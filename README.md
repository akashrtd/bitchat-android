<p align="center">
    <img src="https://github.com/user-attachments/assets/188c42f8-d249-4a72-b27a-e2b4f10a00a8" alt="PeerChat Android Logo" width="480">
</p>

# PeerChat for Android

PeerChat is a secure, decentralized, and resilient peer-to-peer messaging application for Android. It's designed for private, serverless communication, and it works with or without an internet connection.

At its core, PeerChat uses a **Bluetooth mesh network** to connect directly with nearby devices. When you're off-grid, at a crowded event, or in a place with no reliable internet, PeerChat creates a local, self-healing network.

When an internet connection is available, PeerChat can seamlessly switch to **Wi-Fi or mobile data**, allowing you to stay connected with your peers no matter where they are.

This project is built upon the foundation of the original [bitchat iOS app](https://github.com/jackjackbits/bitchat), and it maintains 100% protocol compatibility for cross-platform communication with it.

## Features

- **Hybrid Networking**: Seamlessly switch between Bluetooth mesh and Wi-Fi/Internet.
- **Decentralized Mesh Network**: Automatic peer discovery and multi-hop message relay over Bluetooth LE.
- **End-to-End Encryption**: X25519 key exchange + AES-256-GCM for private messages.
- **Channel-Based Chats**: Topic-based group messaging with optional password protection.
- **Store & Forward**: Messages are cached for offline peers and delivered when they reconnect.
- **Privacy First**: No accounts, no phone numbers, no persistent identifiers.
- **IRC-Style Commands**: A familiar and powerful command-line interface.
- **Modern Android UI**: Built with Jetpack Compose and Material Design 3.

## Getting Started

### Prerequisites

- **Android Studio**: Arctic Fox (2020.3.1) or newer
- **Android SDK**: API level 26 (Android 8.0) or higher
- **Java Development Kit (JDK)**: Version 11 or newer

### Build Instructions

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/akashrtd/peerchat.git
    cd peerchat
    ```
2.  **Open in Android Studio:**
    Open Android Studio and select "Open an Existing Project", then navigate to the `peerchat` directory.
3.  **Build the project:**
    ```bash
    ./gradlew build
    ```
4.  **Install on your device:**
    ```bash
    ./gradlew installDebug
    ```

## Usage

### Basic Commands

- `/j #channel`: Join or create a channel.
- `/m @name message`: Send a private message.
- `/w`: List online users.
- `/connect <nickname> <ip_address>`: Connect to a peer over the internet.
- `/disconnect`: Disconnect from the network.

### How it Works

1.  **Install the app** on your Android device.
2.  **Grant permissions** for Bluetooth, location, and internet.
3.  **Launch PeerChat**. It will automatically start searching for nearby peers via Bluetooth.
4.  **Set your nickname**.
5.  If you're near other PeerChat or bitchat users, you'll connect automatically.
6.  To connect with a remote peer, use the `/connect` command with their nickname and IP address.

## Technical Architecture

PeerChat is built with a modern Android architecture, using Kotlin, Jetpack Compose, and Coroutines. It's divided into several core components:

-   **`BluetoothMeshService`**: Manages the Bluetooth LE mesh network.
-   **`NetworkManager`**: Handles communication over Wi-Fi and mobile data.
-   **`EncryptionService`**: Provides end-to-end encryption using the Noise Protocol Framework.
-   **`ChatViewModel`**: Manages the application's state and business logic.

## Contributing

Contributions are welcome! If you'd like to contribute, please fork the repository and create a pull request. Key areas for enhancement include:

-   **Automated peer discovery over the internet.**
-   **Improved UI/UX.**
-   **Enhanced security features.**
-   **More comprehensive testing.**

## License

This project is released into the public domain. See the [LICENSE.md](LICENSE.md) file for details.
