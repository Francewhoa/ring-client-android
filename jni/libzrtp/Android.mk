#
# Copyright (c) 2013 Slient Circle LLC.  All rights reserved.
#
# @author Werner Dittmann <Werner.Dittmann@t-online.de>
#
# ZRTP version: 4.0.0

ROOT_SRC_PATH := $(LOCAL_PATH)/libzrtp/sources

OLD_PATH = $(LOCAL_PATH)
LOCAL_PATH = libzrtp/sources
#
# Define and build the zrtpcpp static lib
#
include $(CLEAR_VARS)
LOCAL_MODULE := zrtpcpp
LOCAL_CPP_FEATURES := exceptions

# include paths for zrtpcpp modules
LOCAL_C_INCLUDES += $(ROOT_SRC_PATH) $(ROOT_SRC_PATH)/srtp $(ROOT_SRC_PATH)/zrtp $(ROOT_SRC_PATH)/bnlib \
                    $(ROOT_SRC_PATH)/clients/tivi \
					$(ROOT_SRC_PATH)/clients/ccrtp \
					$(ROOT_SRC_PATH)/common

LOCAL_CFLAGS := -DSUPPORT_NON_NIST

LOCAL_SRC_FILES += buildinfo_$(TARGET_ARCH_ABI).c
LOCAL_SRC_FILES += zrtp/ZrtpCallbackWrapper.cpp zrtp/ZRtp.cpp zrtp/ZrtpCrc32.cpp zrtp/ZrtpPacketCommit.cpp zrtp/ZrtpPacketConf2Ack.cpp zrtp/ZrtpPacketConfirm.cpp zrtp/ZrtpPacketDHPart.cpp zrtp/ZrtpPacketGoClear.cpp zrtp/ZrtpPacketClearAck.cpp zrtp/ZrtpPacketHelloAck.cpp zrtp/ZrtpPacketHello.cpp zrtp/ZrtpPacketError.cpp zrtp/ZrtpPacketErrorAck.cpp zrtp/ZrtpPacketPingAck.cpp zrtp/ZrtpPacketPing.cpp zrtp/ZrtpPacketSASrelay.cpp zrtp/ZrtpPacketRelayAck.cpp zrtp/ZrtpStateClass.cpp zrtp/ZrtpTextData.cpp zrtp/ZrtpConfigure.cpp zrtp/ZrtpCWrapper.cpp zrtp/Base32.cpp zrtp/zrtpB64Encode.c zrtp/zrtpB64Decode.c common/osSpecifics.c zrtp/ZrtpSdesStream.cpp zrtp/ZIDCacheDb.cpp zrtp/ZIDRecordDb.cpp zrtp/zrtpCacheSqliteBackend.c clients/tivi/CtZrtpSession.cpp clients/tivi/CtZrtpStream.cpp common/Thread.cpp common/MutexClass.cpp common/EventClass.cpp zrtp/crypto/zrtpDH.cpp zrtp/crypto/hmac256.cpp zrtp/crypto/sha256.cpp zrtp/crypto/hmac384.cpp zrtp/crypto/sha384.cpp zrtp/crypto/aesCFB.cpp zrtp/crypto/twoCFB.cpp zrtp/crypto/sha2.c zrtp/crypto/skeinMac256.cpp zrtp/crypto/skein256.cpp zrtp/crypto/skeinMac384.cpp zrtp/crypto/skein384.cpp srtp/CryptoContext.cpp srtp/CryptoContextCtrl.cpp srtp/SrtpHandler.cpp srtp/crypto/hmac.cpp srtp/crypto/SrtpSymCrypto.cpp srtp/crypto/sha1.c cryptcommon/twofish.c cryptcommon/twofish_cfb.c cryptcommon/aescrypt.c cryptcommon/aeskey.c cryptcommon/aestab.c cryptcommon/aes_modes.c cryptcommon/macSkein.cpp cryptcommon/skein.c cryptcommon/skein_block.c cryptcommon/skeinApi.c cryptcommon/ZrtpRandom.cpp

include $(BUILD_STATIC_LIBRARY)

LOCAL_PATH = $(ROOT_SRC_PATH)/../..