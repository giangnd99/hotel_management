package com.poly.booking.management.dao.booking.mapper.entity;

import com.poly.booking.management.dao.booking.entity.BookingEntity;
import com.poly.booking.management.dao.booking.entity.BookingRoomEntity;
import com.poly.booking.management.dao.room.entity.RoomEntity;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.BookingRoom;
import com.poly.booking.management.domain.entity.Customer;
import com.poly.booking.management.domain.entity.Room;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class BookingDomainToEntityDataAccessMapper {
    /**
     * Map Booking (domain) -> BookingEntity (persistence)
     * Ensures:
     *  - bookingEntity.bookingRooms contains mapped BookingRoomEntity
     *  - each BookingRoomEntity.booking is set to the bookingEntity (no orphan)
     *
     * Adjust this function to your actual domain/entity APIs (constructors/getters/setters).
     */
    public BookingEntity toEntity(Booking booking) {
        if (booking == null) return null;

        // Identity map for BookingEntity instances (keyed by booking id value) in case needed
        Map<Object, BookingEntity> bookingEntityCache = new HashMap<>();

        BookingEntity be = buildBookingEntityShallow(booking);
        bookingEntityCache.put(extractIdValue(booking.getId()), be);

        // Map booking rooms
        List<BookingRoom> rooms = safeList(booking.getBookingRooms());
        List<BookingRoomEntity> roomEntities = rooms.stream()
                .map(br -> bookingRoomToEntity(br, be, bookingEntityCache))
                .collect(Collectors.toList());

        // Ensure each BookingRoomEntity.booking points to this booking entity
        roomEntities.forEach(re -> re.setBooking(be));

        be.setBookingRooms(roomEntities);

        return be;
    }

    /**
     * Build a shallow BookingEntity from domain Booking: set primitive fields only (not rooms).
     */
    private BookingEntity buildBookingEntityShallow(Booking booking) {
        BookingEntity be = new BookingEntity();

        // id: domain BookingId -> entity id
        if (booking.getId() != null) {
            Object idVal = extractIdValue(booking.getId());
            setFieldIfExists(be, "setId", idVal);
        }

        // check in / out
        if (booking.getCheckInDate() != null) {
            Object dt = convertDateCustomToLocalDateTime(booking.getCheckInDate());
            setFieldIfExists(be, "setCheckIn", dt);
        }
        if (booking.getCheckOutDate() != null) {
            Object dt = convertDateCustomToLocalDateTime(booking.getCheckOutDate());
            setFieldIfExists(be, "setCheckOut", dt);
        }

        // total price
        if (booking.getTotalPrice() != null) {
            Object amount = extractMoneyAmount(booking.getTotalPrice());
            setFieldIfExists(be, "setTotalPrice", amount);
        }

        // status (enum -> name)
        if (booking.getStatus() != null) {
            setFieldIfExists(be, "setStatus", booking.getStatus().name());
        }

        // actual check in/out
        if (booking.getActualCheckInDate() != null) {
            setFieldIfExists(be, "setActualCheckIn", convertDateCustomToLocalDateTime(booking.getActualCheckInDate()));
        }
        if (booking.getActualCheckOutDate() != null) {
            setFieldIfExists(be, "setActualCheckOut", convertDateCustomToLocalDateTime(booking.getActualCheckOutDate()));
        }

        // tracking id
        if (booking.getTrackingId() != null) {
            Object tid = extractTrackingIdValue(booking.getTrackingId());
            setFieldIfExists(be, "setTrackingId", tid);
        }

        // customer -> set customer id if possible
        if (booking.getCustomer() != null) {
            Object cid = tryExtractCustomerIdValue(booking.getCustomer());
            setFieldIfExists(be, "setCustomerId", cid);
        }

        // number of guests -> set number of Guests id if possible
        if (booking.getNumberOfGuests() != null) {
            Object cid = tryExtractNumberOfGuests(booking.getNumberOfGuests());
            setFieldIfExists(be, "setNumberOfGuest", cid);
        }

        return be;
    }

    /**
     * Map BookingRoom (domain) -> BookingRoomEntity (persistence).
     * If parentBookingEntity != null, set booking reference to it (preferred).
     * Uses bookingEntityCache to reuse existing BookingEntity instances.
     */
    private BookingRoomEntity bookingRoomToEntity(BookingRoom br, BookingEntity parentBookingEntity, Map<Object, BookingEntity> bookingEntityCache) {
        BookingRoomEntity bre = new BookingRoomEntity();

        // id
        if (br.getId() != null) {
            Object idVal = extractIdValue(br.getId());
            setFieldIfExists(bre, "setId", idVal);
        }

        // price
        if (br.getPrice() != null) {
            Object amount = extractMoneyAmount(br.getPrice());
            setFieldIfExists(bre, "setPrice", amount);
        }

        // room: either set roomId or full RoomEntity depending on your entity model
        if (br.getRoom() != null) {
            Room r = br.getRoom();
            // try setRoomId
            Object rid = tryExtractRoomIdValue(r);
            if (rid != null) {
                setFieldIfExists(bre, "setRoomId", rid);
            } else {
                // else try setRoom with RoomEntity
                RoomEntity re = buildRoomEntityFromDomain(r);
                setFieldIfExists(bre, "setRoom", re);
            }
        }

        // booking: prefer set to parentBookingEntity
        if (parentBookingEntity != null) {
            bre.setBooking(parentBookingEntity);
        } else {
            // if domain BookingRoom has booking reference, try to map it
            try {
                Booking bRef = br.getBooking();
                if (bRef != null) {
                    Object bid = extractIdValue(bRef.getId());
                    BookingEntity cached = bookingEntityCache.get(bid);
                    if (cached == null) {
                        // create shallow booking entity for reference
                        BookingEntity created = buildBookingEntityShallow(bRef);
                        bookingEntityCache.put(bid, created);
                        bre.setBooking(created);
                    } else {
                        bre.setBooking(cached);
                    }
                }
            } catch (Exception ignored) {
            }
        }

        return bre;
    }

    // ---------- Helpers / Reflection-friendly utilities ----------

    private <T> List<T> safeList(List<T> l) {
        return l == null ? Collections.emptyList() : l;
    }

    /**
     * Extract raw id value from a value object (e.g., BookingId.getValue()).
     * If object is primitive (Long, String), returns it directly.
     * If valueObject has method getValue(), invoke it.
     */
    private Object extractIdValue(Object idObj) {
        if (idObj == null) return null;
        // if primitive id type (Long, Integer, String, UUID), return directly
        if (idObj instanceof Number || idObj instanceof String || idObj instanceof java.util.UUID) return idObj;
        // else try getValue()
        try {
            Method m = idObj.getClass().getMethod("getValue");
            return m.invoke(idObj);
        } catch (Exception ignored) {
        }
        // fallback: try toString
        return idObj.toString();
    }

    private Object extractMoneyAmount(Object moneyObj) {
        if (moneyObj == null) return null;
        try {
            Method m = moneyObj.getClass().getMethod("getAmount");
            return m.invoke(moneyObj);
        } catch (Exception ignored) {}
        // if it's numeric already
        if (moneyObj instanceof Number) return moneyObj;
        return null;
    }

    private Object extractTrackingIdValue(Object trackingIdObj) {
        if (trackingIdObj == null) return null;
        try {
            Method m = trackingIdObj.getClass().getMethod("getValue");
            return m.invoke(trackingIdObj);
        } catch (Exception ignored) {}
        return trackingIdObj.toString();
    }

    private Object tryExtractNumberOfGuests(Integer numberOfGuests) {
        if (numberOfGuests == null) return null;
        try {
            Method m = numberOfGuests.getClass().getMethod("getNumberOfGuests");
            Object idObj = m.invoke(m);
            return extractIdValue(idObj);
        } catch (Exception ignored) {}
        return null;
    }
    private Object tryExtractCustomerIdValue(Customer customer) {
        if (customer == null) return null;
        try {
            Method m = customer.getClass().getMethod("getId");
            Object idObj = m.invoke(customer);
            return extractIdValue(idObj);
        } catch (Exception ignored) {}
        return null;
    }

    private Object tryExtractRoomIdValue(Room room) {
        if (room == null) return null;
        try {
            Method m = room.getClass().getMethod("getId");
            Object idObj = m.invoke(room);
            return extractIdValue(idObj);
        } catch (Exception ignored) {}
        return null;
    }

    private RoomEntity buildRoomEntityFromDomain(Room r) {
        if (r == null) return null;
        RoomEntity re = new RoomEntity();
        Object rid = tryExtractRoomIdValue(r);
        if (rid != null) {
            setFieldIfExists(re, "setId", rid);
        }
        // set basic fields if exist on domain Room
        try {
            Method getNumber = r.getClass().getMethod("getRoomNumber");
            Object rn = getNumber.invoke(r);
            setFieldIfExists(re, "setRoomNumber", rn);
        } catch (Exception ignored) {}
        try {
            Method getPrice = r.getClass().getMethod("getBasePrice");
            Object p = getPrice.invoke(r);
            setFieldIfExists(re, "setPrice", p);
        } catch (Exception ignored) {}
        try {
            Method getStatus = r.getClass().getMethod("getStatus");
            Object s = getStatus.invoke(r);
            setFieldIfExists(re, "setStatus", s);
        } catch (Exception ignored) {}
        return re;
    }

    private Object convertDateCustomToLocalDateTime(Object dateCustom) {
        if (dateCustom == null) return null;
        try {
            // Nếu DateCustom có sẵn method toLocalDateTime()
            Method m = dateCustom.getClass().getMethod("getValue");
            return m.invoke(dateCustom);
        } catch (NoSuchMethodException e) {
            // Nếu không, fallback: dùng toLocalDate() + set giờ mặc định
            try {
                Method m2 = dateCustom.getClass().getMethod("toLocalDate");
                Object ld = m2.invoke(dateCustom);
                if (ld instanceof java.time.LocalDate) {
                    return ((java.time.LocalDate) ld).atStartOfDay(); // 00:00
                }
            } catch (Exception ignored) {}
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * General-purpose helper: try to call setter method by name on target with given value.
     * If method missing or type mismatch, ignore silently.
     */
    private void setFieldIfExists(Object target, String setterName, Object value) {
        if (target == null || setterName == null || value == null) return;
        try {
            Method[] methods = target.getClass().getMethods();
            for (Method m : methods) {
                if (!m.getName().equals(setterName)) continue;
                Class<?>[] params = m.getParameterTypes();
                if (params.length != 1) continue;
                try {
                    m.invoke(target, value);
                    return;
                } catch (IllegalArgumentException iae) {
                    Class<?> pType = params[0];
                    Object converted = tryConvertSimple(value, pType);
                    if (converted != null) {
                        m.invoke(target, converted);
                        return;
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private Object tryConvertSimple(Object value, Class<?> targetType) {
        if (value == null) return null;
        if (targetType.isAssignableFrom(value.getClass())) return value;
        try {
            if (Number.class.isAssignableFrom(value.getClass())) {
                Number n = (Number) value;
                if (targetType == Long.class || targetType == long.class) return n.longValue();
                if (targetType == Integer.class || targetType == int.class) return n.intValue();
                if (targetType == Double.class || targetType == double.class) return n.doubleValue();
                if (targetType == Float.class || targetType == float.class) return n.floatValue();
                if (targetType == Short.class || targetType == short.class) return n.shortValue();
                if (targetType == Byte.class || targetType == byte.class) return n.byteValue();
            }
            if (targetType == String.class) return value.toString();
            if (targetType == java.util.UUID.class) {
                if (value instanceof String) return java.util.UUID.fromString((String) value);
            }
        } catch (Exception ignored) {}
        return null;
    }
}
