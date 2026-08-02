# DaysLite currently has no reflection-based application entry points that
# require custom keep rules. Android Gradle Plugin supplies rules for manifest
# components and Android framework callbacks.
#
# If reflection or annotation-driven serialization is added later, keep only
# the required classes or members instead of disabling R8 for the whole app.
