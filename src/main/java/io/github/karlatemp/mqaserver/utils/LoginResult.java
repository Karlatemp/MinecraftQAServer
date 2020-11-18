package io.github.karlatemp.mqaserver.utils;

import java.util.Objects;

public class LoginResult {

    private String id;
    private String name;
    private Property[] properties;

    public LoginResult(String id, String name, Property[] properties) {
        this.id = id;
        this.name = name;
        this.properties = properties;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Property[] getProperties() {
        return this.properties;
    }

    public void setProperties(Property[] properties) {
        this.properties = properties;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LoginResult)) return false;
        final LoginResult other = (LoginResult) o;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        return java.util.Arrays.deepEquals(this.getProperties(), other.getProperties());
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LoginResult;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        result = result * PRIME + java.util.Arrays.deepHashCode(this.getProperties());
        return result;
    }

    public String toString() {
        return "LoginResult(id=" + this.getId() + ", name=" + this.getName() + ", properties=" + java.util.Arrays.deepToString(this.getProperties()) + ")";
    }

    public static class Property {

        private String name;
        private String value;
        private String signature;

        public Property(String name, String value, String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getSignature() {
            return this.signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof Property)) return false;
            final Property other = (Property) o;
            if (!other.canEqual(this)) return false;
            final Object this$name = this.getName();
            final Object other$name = other.getName();
            if (!Objects.equals(this$name, other$name)) return false;
            final Object this$value = this.getValue();
            final Object other$value = other.getValue();
            if (!Objects.equals(this$value, other$value)) return false;
            final Object this$signature = this.getSignature();
            final Object other$signature = other.getSignature();
            return Objects.equals(this$signature, other$signature);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof Property;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $name = this.getName();
            result = result * PRIME + ($name == null ? 43 : $name.hashCode());
            final Object $value = this.getValue();
            result = result * PRIME + ($value == null ? 43 : $value.hashCode());
            final Object $signature = this.getSignature();
            result = result * PRIME + ($signature == null ? 43 : $signature.hashCode());
            return result;
        }

        public String toString() {
            return "LoginResult.Property(name=" + this.getName() + ", value=" + this.getValue() + ", signature=" + this.getSignature() + ")";
        }
    }
}
