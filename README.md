# ChatU - Real-Time Android Chat Application

ChatU is a modern, real-time messaging application for Android that enables users to connect and communicate seamlessly. Built with Firebase backend services, the app provides a smooth and responsive chat experience with user authentication and profile management.

## ✨ Features

- **User Authentication**: Secure login and registration system using Firebase Authentication
- **Real-Time Messaging**: Instant messaging with Firebase Realtime Database
- **User Profiles**: View and update user profiles with profile pictures
- **User Discovery**: Browse and connect with other registered users
- **Status Updates**: Set and display custom user statuses
- **Modern UI**: Clean and intuitive Material Design interface
- **Profile Pictures**: Support for user profile images with Picasso image loading

## 🛠️ Technologies Used

- **Language**: Java
- **Platform**: Android (Min SDK 21, Target SDK 33)
- **Backend**: Firebase
  - Firebase Authentication
  - Firebase Realtime Database
  - Firebase Storage
- **Libraries**:
  - AndroidX AppCompat
  - Material Design Components
  - Picasso for image loading
  - ConstraintLayout

## 📱 App Structure

The application consists of several key activities:
- **MainActivity**: Splash screen with authentication check
- **LoginActivity**: User login interface
- **RegisterActivity**: New user registration
- **HomeActivity**: Main screen displaying user list
- **ChatActivity**: One-on-one messaging interface
- **UserProfileActivity**: View and edit user profiles

## 🚀 Getting Started

### Prerequisites
- Android Studio (Latest version recommended)
- Android SDK (API Level 21 or higher)
- Firebase project with Authentication, Realtime Database, and Storage enabled

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/dhruv-0707/ChatU.git
   ```

2. Open the project in Android Studio

3. Add your `google-services.json` file from Firebase Console to the `app/` directory

4. Sync Gradle and build the project

5. Run the app on an emulator or physical device

## 📄 License

This project is open source and available for educational purposes.

## 👤 Developer

Developed by Dhruv

---

**About Section Description (Short Version)**:
> ChatU is a real-time Android messaging app built with Firebase. Features include user authentication, instant messaging, profile management, and a modern Material Design interface. Perfect for learning Android development with Firebase integration.
