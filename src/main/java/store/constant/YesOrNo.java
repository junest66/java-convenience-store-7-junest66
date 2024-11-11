package store.constant;

public enum YesOrNo {
    YES("Y"),
    NO("N");

    private final String value;

    YesOrNo(String value) {
        this.value = value;
    }

    public static YesOrNo of(String value) {
        for (YesOrNo yesOrNo : YesOrNo.values()) {
            if (yesOrNo.value.equals(value)) {
                return yesOrNo;
            }
        }
        throw new IllegalArgumentException("Y 또는 N을 입력해주세요.");
    }
}
