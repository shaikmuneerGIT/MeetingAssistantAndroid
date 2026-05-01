# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in the SDK tools.

# Keep Gson serialization
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.meetingassistant.app.data.models.** { *; }
-keep class com.meetingassistant.app.services.LLMService$* { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
