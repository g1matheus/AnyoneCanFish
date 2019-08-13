# AnyoneCanFish
Just an app (and widget) with fishing info (weather data + solunar data) for your area,
with a suggestion of the chance of success.
Basic fishing info as well as a brief description of a small selection of common game fish is also easily accessible.

Ongoing development is to fully realize the Trip and Tackle Box functionality.

*** IMPORTANT NOTE: ***
Please use "Email" instead of "Google" to authenticate.
- Explanation:
Using "Google" as a sign-in provider will result in a "code 10: message 10" toast/error for you. 
This is because your signing certificate (from the Android Studio on the comp with which you are deploying to a device/emulator)
is not included in the project settings of the Firebase project this program uses. Google authentication *does* work,
but only for devices I deploy to. This won't be an issue once released.

