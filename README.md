# HealthPal
* HealthPal is a integrated health companion for people of all ages. 
* HealthPal is a Android Application.
* CompileSDKVersion 26
* minSDKVersion 21

## Main Points
* Simplistic UI design 
* Basic email and password login with Firebase
* Integrate Facebook login with Firebase
* Homepage contains bottomNavigationBar with Viewpager, 3 fragments
* home fragment contains 2 buttons, to explorer and healthpal page
* explorer contains some of the popular illness displayed in a staggeredGridLayout. Data is fetched from APIMedic 
* ##### APIMedic API “as mentioned earlier,  as the token is valid for 2 hours only, this version will stop working once the two-hour time period has expired. ” So the explorer part of the app is built with this API, hence it may not be accessible until I manually update the token, as it is a sandboxed API with limited usage per token
* 
* Upon clicking each illness, detailed information of each illness is presented, again using APIMNedic. 
* Back to HealthPal page, its an implementation of IBM Watson Conversationist as a mobile chat bot to enquire about body pain ( as of now there are only a few intent about some parts of the body). 
* you can have small talk with the the assistant for example, asking her who is she, how is she, or request a joke from her
* Also you can ask her how to use the app and she will tell you what to do
* Next on to the Map Page, i integrated Google Map and Google Places API, to show the current location as well as search for some type of places that i predefined. For example, pharmacy, clinic, hospital. 
* Direction and intent to google map is also integrated, but its the default google map set up.
* Next the profile page pulls information from the user info from firebase and display it, mainly just username and email for now. 
* Step counter is implemented using the native Sensor API in android and it is calculated to be sent to firebase and synced up every time the fragment loads
