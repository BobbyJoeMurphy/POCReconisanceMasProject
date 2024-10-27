# Automated Network Defence System with JADE

This project leverages the JADE (Java Agent DEvelopment Framework) to implement an automated network defense system, focusing on real-time monitoring and blocking of suspicious IP addresses.

## Overview

The automated network defense system uses a multi-agent architecture to enhance network security by detecting and mitigating potential Denial of Service (DoS) attacks. The setup consists of two main agents:

- **Reconnaissance Agent**: Detects anomalies in network traffic, specifically targeting ICMP ping requests to identify suspicious activities.
- **Action Agent**: Dynamically updates pfSense firewall rules to block identified threats, automating the response to potential attacks.

## Features

- **Real-Time Monitoring**: Continuously monitors network traffic for suspicious IP addresses.
- **Automated Threat Detection**: Uses the Reconnaissance Agent to detect anomalies and suspicious patterns.
- **Dynamic Firewall Updates**: The Action Agent automatically adjusts pfSense firewall rules based on detected threats.
- **Enhanced Security**: Reduces reliance on manual interventions and ensures faster response times to potential attacks.
- As you can imagine, ive redacted other functionalitys of this project for reasonable ip saftey.

### Prerequisites

- Java Development Kit (JDK) installed
- JADE framework
- pfSense firewall

### Installation

1. Clone the repository:
```https://github.com/BobbyJoeMurphy/POCReconisanceMasProject.git

