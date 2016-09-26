package com.merle.util.uuid;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import java.nio.ByteBuffer;
import java.util.UUID;

public abstract class UUIDUtils {
//
//    public static String uuid() {
//        UUID uuid = UUID.randomUUID();
//        return StringUtils.trim(uuid.toString());
//    }
//

//
//    public static String encodeBase64Uuid(String uuidString) {
//        UUID uuid = UUID.fromString(uuidString);
//        return StringUtils.trim(base64Uuid(uuid));
//    }
//
//    public static String decodeBase64Uuid(String compressedUuid) {
//        byte[] byUuid = Base64.decodeBase64(compressedUuid);
//        ByteBuffer bb = ByteBuffer.wrap(byUuid);
//        UUID uuid = new UUID(bb.getLong(), bb.getLong());
//        return StringUtils.trim(uuid.toString());
//    }


    public static String uuidHibernate() {
        return StringUtils.trim(UUIDHexGenerator.getInstance().getId());
    }

    public static String base64Uuid() {
        UUID uuid = UUID.randomUUID();
        return StringUtils.trim(base64Uuid(uuid));
    }

    public static String base58Uuid() {
        UUID uuid = UUID.randomUUID();
        return StringUtils.trim(base58Uuid(uuid));
    }

    private static String base64Uuid(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return Base64.encodeBase64URLSafeString(bb.array());
    }

    private static String base58Uuid(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return Base58.encode(bb.array());
    }

    public static void main(String[] args) {
        while (true) {
            UUIDUtils.base58Uuid();
        }
    }


//
//    public static String encodeBase58Uuid(String uuidString) {
//        UUID uuid = UUID.fromString(uuidString);
//        return base58Uuid(uuid);
//    }
//
//    public static String decodeBase58Uuid(String base58uuid) {
//        byte[] byUuid = Base58.decode(base58uuid);
//        ByteBuffer bb = ByteBuffer.wrap(byUuid);
//        UUID uuid = new UUID(bb.getLong(), bb.getLong());
//        return uuid.toString();
//    }
}