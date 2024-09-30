# Courtify - Tennis Court Availability and Player Finder App

**Courtify** is an Android application designed to enhance the tennis playing experience by helping users find available courts in real-time and connect with other players based on age, skill level, and location. Utilizing the power of **YOLOv8** and **OpenCV**, the app performs real-time people detection on tennis courts to display availability, and leverages **Firebase** for data storage, ensuring seamless updates and connectivity.

## Key Features

- **Real-time Court Availability**: Detects the number of people present on tennis courts using **YOLOv8** and **OpenCV** for real-time object detection.
- **Player Matching**: Connect with players based on age, skill level, and location.
- **Firebase Integration**: Ensures real-time data updates and secure storage for player and court information.

## Technology Stack

- **YOLOv8**: Used for object detection to count the number of people on tennis courts.
- **OpenCV**: Integrated for real-time image processing and object detection.
- **Firebase**: Provides real-time database functionality for storing and retrieving data related to court availability and player details.
- **Android Studio**: Primary development environment for building the app.
  
## Application Flow

1. **Detect Court Availability**:
   - **YOLOv8** combined with **OpenCV** is used to scan and count the number of people on a court in real-time.
   - The user can view available courts based on current occupancy data, ensuring minimal wait time to play.

   ![Court Detection](https://imgur.com/QBevLj6.png)

2. **Connect with Players**:
   - Users can search for tennis partners based on **age**, **skill level**, or a combination of both. This allows players to find the best match for their playing style and availability.
   
   ![Player Search](https://imgur.com/EummAfV.png)

3. **Player Profile**:
   - The app allows users to view detailed player profiles, including name, skill level, age, and location. They can then send a message to connect and schedule a game.
   
   ![Player Profile](https://imgur.com/5FW5Fe04k8wHuJfITvybS94A.png)

## Installation

### Prerequisites

- **Android Studio**: Install [Android Studio](https://developer.android.com/studio) to develop, test, and deploy the app.
- **Firebase Account**: Set up a Firebase project to handle real-time data storage for user and court availability data.
  
### Steps to Set Up the Project

1. Clone the repository:
    ```bash
    git clone https://github.com/username/Courtify.git
    cd Courtify
    ```

2. Open the project in **Android Studio** and sync Gradle files to ensure all dependencies are installed.

3. Configure Firebase:
    - Add the `google-services.json` file from your Firebase console to the `/app` directory.
    - Set up Firebase Realtime Database for storing player and court data.
   
4. Build and run the app on your Android device.

## Usage

1. **View Court Availability**:
    - Open the app to view real-time tennis court availability.
    - The court availability is determined by real-time people detection using **YOLOv8**.

2. **Find Tennis Partners**:
    - Use the player search functionality to find tennis players based on age, skill level, or both.
    - Send connection requests to potential partners.

3. **Manage Player Profile**:
    - Customize your profile with age, skill level, and location to connect with other players.

## Future Enhancements

- **Location-based Notifications**: Notify users when a court becomes available at their preferred location.
- **Skill-based Match Suggestions**: Provide intelligent matchmaking based on playing history and performance data.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any questions or issues, please contact:

- **Ansh Tiwari** (anshtiwari9899@gmail.com)

