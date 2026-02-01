# Washing Schedule App 

Android application for managing laundry schedules in residential buildings, built with Kotlin, MVVM, Hilt, Firebase, and Jetpack Compose.

##  Key Features:

-  Multi-building support with unlimited users.  
-  Laundry booking system with creation, editing, and weekly usage limits.  
-  Events are automatically reset every week via Cloud Functions.  
-  Event statuses are updated hourly (Active / Finished).  
-  Built-in admin panel within the app.  
-  Authentication with Firebase Auth (Email/Password).  
-  Real-time database using Firestore.  
-  Custom animations, CircularProgressBar, and navigation with NavGraph.  
-  MVVM + Hilt architecture.
-  Custom procedural schedule view fully built from an empty layout, dynamically rendering events and time slots in Jetpack Compose.
-  Advanced schedule validation to prevent overlapping bookings.

##  Technologies Used
- Kotlin, Jetpack Compose  
- Hilt (Dependency Injection)  
- Firebase (Auth, Firestore, Functions)  
- Navigation Component  
- Material 3, Lottie Animations

##  Architecture
The project follows a clean MVVM architecture with well-defined layers:
- View → Composables and navigation.  
- ViewModel → UI logic and state handling with StateFlow.    
- Cloud Functions → Automates weekly resets and hourly event updates.

##  Screenshots
Watch the demo https://github.com/user-attachments/assets/9150587f-0ebe-4df5-8334-ddaac49fbe0a
Watch admin view https://github.com/user-attachments/assets/81340e60-c57c-40cf-94f9-edf73d03a525

##  Author
**Jesús Ruiz**  
[LinkedIn](www.linkedin.com/in/jesus-ru1z-20-08-02-lmm) | [Email](jesusitoruiz89@gmail.com)
