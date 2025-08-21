package com.poly.booking.management.dao.booking.mapper.domain;

import com.poly.booking.management.dao.booking.entity.BookingEntity;
import com.poly.booking.management.dao.booking.entity.BookingRoomEntity;
import com.poly.booking.management.dao.room.entity.RoomEntity;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.BookingRoom;
import com.poly.booking.management.domain.entity.Customer;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.valueobject.BookingRoomId;
import com.poly.booking.management.domain.valueobject.TrackingId;
import com.poly.domain.valueobject.BookingId;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.Money;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BookingEntityToDomainDataAccessMapper {

    /**
     * Map BookingEntity -> Booking (domain), đảm bảo single Booking object referenced by its BookingRoom.
     */
    public Booking toDomainEntity(BookingEntity bookingEntity) {
        if (bookingEntity == null) return null;

        // Identity map: key là id value type của entity (type generic Object vì có thể là Long, UUID, ...)
        Map<Object, Booking> bookingCache = new HashMap<>();

        // 1) Build placeholder Booking (all fields except bookingRooms)
        Booking placeholder = buildPlaceholderBooking(bookingEntity);
        bookingCache.put(bookingEntity.getId(), placeholder);

        // 2) Map bookingRooms referencing placeholder (avoid mapping booking fully again inside each room)
        List<BookingRoomEntity> bookingRoomEntities = safeList(bookingEntity.getBookingRooms());
        List<BookingRoom> bookingRooms = bookingRoomEntities.stream()
                .map(brEntity -> toDomainBookingRoom(brEntity, placeholder, bookingCache))
                .collect(Collectors.toList());

        // 3) Rebuild final booking with the mapped rooms
        Booking finalBooking = rebuildBookingWithRooms(placeholder, bookingRooms);

        // 4) Ensure each BookingRoom.booking references finalBooking (if domain allows mutation)
        //    We already set parent while creating BookingRoom where possible. For safety, try to set again
        //    using setter if available.
        injectParentBookingToRooms(bookingRooms, finalBooking);

        return finalBooking;
    }

    // ---------- Private helpers ----------

    /**
     * Tạo placeholder Booking (không set bookingRooms) để dùng làm parent khi map từng BookingRoom.
     * Điều chỉnh builder/các field nếu domain API khác.
     */
    private Booking buildPlaceholderBooking(BookingEntity be) {
        // Sử dụng builder như file gốc (nếu domain dùng constructor khác, đổi lại ở đây)
        Booking.Builder b = Booking.Builder.builder()
                .id(new BookingId(be.getId()))
                .checkInDate(be.getCheckIn() != null ? DateCustom.of(be.getCheckIn()) : null)
                .checkOutDate(be.getCheckOut() != null ? DateCustom.of(be.getCheckOut()) : null)
                .totalPrice(be.getTotalPrice() != null ? new Money(be.getTotalPrice()) : null)
                .status(be.getStatus() != null ? BookingStatus.valueOf(be.getStatus()) : null)
                .actualCheckInDate(be.getActualCheckIn() != null ? DateCustom.of(be.getActualCheckIn()) : null)
                .actualCheckOutDate(be.getActualCheckOut() != null ? DateCustom.of(be.getActualCheckOut()) : null)
                .trackingId(be.getTrackingId() != null ? new TrackingId(be.getTrackingId()) : null);

        // map customer nếu có (file gốc dùng UUID cho customerId)
        if (be.getCustomerId() != null) {
            Customer customer = Customer.Builder.builder()
                    .id(new com.poly.domain.valueobject.CustomerId(be.getCustomerId()))
                    .build();
            b.customer(customer);
        }

        // Important: đặt danh sách rỗng ở placeholder để tránh null
        b.bookingRooms(Collections.emptyList());
        return b.build();
    }

    /**
     * Map một BookingRoomEntity -> BookingRoom domain, ưu tiên gán parentBooking nếu có (tránh tạo Booking duplicate).
     * bookingCache giữ các Booking đã tạo/placeholder theo id.
     * Lưu ý: BookingRoomEntity trong project của bạn có thể chứa chỉ bookingId hoặc full booking entity.
     */
    private BookingRoom toDomainBookingRoom(BookingRoomEntity brEntity, Booking parentBooking, Map<Object, Booking> bookingCache) {
        if (brEntity == null) return null;

        // Lấy parentBooking nếu truyền null: tìm từ bookingCache theo brEntity.getBooking().getId() (nếu exists)
        Booking parent = parentBooking;
        try {
            if (parent == null && brEntity.getBooking() != null) {
                Object bid = brEntity.getBooking().getId();
                parent = bookingCache.get(bid);
            }
        } catch (Exception ignored) {
        }

        // Map Room reference: BookingRoomEntity có thể chỉ chứa roomId (UUID) hoặc full room entity.
        Room room = null;
        try {
            // nếu có method getRoomId (UUID)
            try {
                java.lang.reflect.Method m = brEntity.getClass().getMethod("getRoomId");
                Object rid = m.invoke(brEntity);
                if (rid != null) {
                    room = Room.Builder.builder().id(new com.poly.domain.valueobject.RoomId((UUID) rid)).build();
                }
            } catch (NoSuchMethodException ignore) {
                // fallback: try getRoom (RoomEntity)
                try {
                    java.lang.reflect.Method m2 = brEntity.getClass().getMethod("getRoom");
                    Object roomEntity = m2.invoke(brEntity);
                    if (roomEntity instanceof RoomEntity) {
                        RoomEntity re = (RoomEntity) roomEntity;
                        room = Room.Builder.builder()
                                .id(new com.poly.domain.valueobject.RoomId(re.getId()))
                                .roomNumber(re.getRoomNumber())
                                .basePrice(new Money(re.getPrice()))
                                .status(re.getStatus())
                                .build();
                    }
                } catch (NoSuchMethodException __) {
                    // nothing to do
                }
            }
        } catch (Exception ignored) {}

        BookingRoom.Builder brBuilder = BookingRoom.Builder.builder()
                .id(new BookingRoomId(brEntity.getId()))
                .room(room)
                .price(brEntity.getPrice() != null ? new Money(brEntity.getPrice()) : null);

        // Set booking reference pointing to parent if available
        if (parent != null) {
            brBuilder.booking(parent);
        } else {
            // nếu không có parent, cố gắng tạo lightweight booking chỉ với id để tránh null
            try {
                if (brEntity.getBooking() != null) {
                    UUID bid = brEntity.getBooking().getId();
                    Booking light = bookingCache.get(bid);
                    if (light == null) {
                        light = Booking.Builder.builder()
                                .id(new BookingId(bid))
                                .bookingRooms(Collections.emptyList())
                                .build();
                        bookingCache.put(bid, light);
                    }
                    brBuilder.booking(light);
                }
            } catch (Exception ignored) {}
        }

        return brBuilder.build();
    }

    /**
     * Rebuild final Booking từ placeholder + danh sách BookingRoom đã map.
     */
    private Booking rebuildBookingWithRooms(Booking placeholder, List<BookingRoom> rooms) {
        Booking.Builder fb = Booking.Builder.builder()
                .id(placeholder.getId())
                .customer(placeholder.getCustomer())
                .checkInDate(placeholder.getCheckInDate())
                .checkOutDate(placeholder.getCheckOutDate())
                .totalPrice(placeholder.getTotalPrice())
                .status(placeholder.getStatus())
                .actualCheckInDate(placeholder.getActualCheckInDate())
                .actualCheckOutDate(placeholder.getActualCheckOutDate())
                .trackingId(placeholder.getTrackingId())
                .bookingRooms(rooms);

        return fb.build();
    }

    /**
     * Nếu domain BookingRoom có setter setBooking(Booking), cập nhật lại để đảm bảo tham chiếu là finalBooking.
     * Nếu không có setter (immutable), thì chúng ta đã cố gắng set booking khi build room (nếu domain builder hỗ trợ).
     */
    private void injectParentBookingToRooms(List<BookingRoom> rooms, Booking parent) {
        if (rooms == null || parent == null) return;

        for (BookingRoom br : rooms) {
            try {
                java.lang.reflect.Method setBooking = br.getClass().getMethod("setBooking", Booking.class);
                setBooking.invoke(br, parent);
            } catch (NoSuchMethodException e) {
                // no setter -> model immutable, do nothing (we constructed with parent where possible)
            } catch (Exception ignored) {
            }
        }
    }

    // ---------- Utility ----------

    private <T> List<T> safeList(List<T> l) {
        return l == null ? Collections.emptyList() : l;
    }
}
