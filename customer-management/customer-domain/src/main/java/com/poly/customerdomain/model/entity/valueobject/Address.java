package com.poly.customerdomain.model.entity.valueobject;

public class Address {

    private String street;
    private String ward;
    private String district;
    private String city;

    // Có thể giữ MIN để gợi ý UI, nhưng không ép buộc nữa
    private static final int MAX_ADDRESS_LENGTH = 100;

    public Address(String street, String ward, String district, String city) {
        this.street  = normalize(street);
        this.ward    = normalize(ward);
        this.district= normalize(district);
        this.city    = normalize(city);
    }

    /** Cho phép null/blank, chỉ trim và giới hạn max length. */
    private static String normalize(String s) {
        if (s == null) return "";
        String t = s.trim();
        if (t.length() > MAX_ADDRESS_LENGTH) {
            t = t.substring(0, MAX_ADDRESS_LENGTH);
        }
        return t;
    }

    /** Địa chỉ rỗng (tất cả phần đều trống) */
    public static Address empty() {
        return new Address("", "", "", "");
    }

    /** Ghép các phần KHÔNG rỗng, ngăn cách bằng ", " */
    public String toFullAddress() {
        StringBuilder sb = new StringBuilder();
        appendIfNotBlank(sb, street);
        appendIfNotBlank(sb, ward);
        appendIfNotBlank(sb, district);
        appendIfNotBlank(sb, city);
        return sb.length() == 0 ? "" : sb.substring(2); // bỏ ", " đầu tiên
    }

    private static void appendIfNotBlank(StringBuilder sb, String part) {
        if (part != null && !part.isBlank()) {
            sb.append(", ").append(part);
        }
    }

    /** Parse từ chuỗi đầy đủ. Cho phép thiếu phần, không ném lỗi. */
    public static Address from(String fullAddress) {
        if (fullAddress == null || fullAddress.isBlank()) {
            return Address.empty();
        }
        String[] parts = fullAddress.split(",\\s*");
        String street   = parts.length > 0 ? parts[0] : "";
        String ward     = parts.length > 1 ? parts[1] : "";
        String district = parts.length > 2 ? parts[2] : "";
        String city     = parts.length > 3 ? parts[3] : "";
        return new Address(street, ward, district, city);
    }

    /** Factory cho trường hợp truyền rời từng phần (cho phép 1–4 phần). */
    public static Address from(String street, String ward, String district, String city) {
        return new Address(street, ward, district, city);
    }

    // (Tùy chọn) Getter nếu cần map ra DTO/JPA
    public String getStreet() { return street; }
    public String getWard() { return ward; }
    public String getDistrict() { return district; }
    public String getCity() { return city; }
}
