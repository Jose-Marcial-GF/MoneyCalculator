#  Money Calculator 

> **Academic Project**
> * **Course:** Software Engineering II 
> * **Institution:** Universidad de Las Palmas de Gran Canaria (ULPGC)

A robust Desktop Application built with **Java Swing** for real-time currency exchange and historical exchange rate visualization.

This project implements the **Command Pattern** and adheres to **SOLID principles** by using the architecture **`model-view-controller`**


## 🚀 Features

### 💱 Currency Exchange
* **Real-time Conversion:** Convert amounts between a wide range of global currencies instantly.
* **Live Rates:** Fetches the latest data from the [Frankfurter API](https://api.frankfurter.dev/).

### 📈 Historical Data Visualization
* **Interactive Charts:** Visualize exchange rate trends over time.
* **Time Periods:** Supports quick filtering for:
    * Week, Month, 6 Months, Year, 5 Years, and Max.
* **Visual Indicators:** Charts automatically color-code trends (Green for rising, Red for falling).

### ⚙️ Live Settings (Non-Blocking)
* **Instant Feedback:** Toggle chart gridlines (Horizontal/Vertical) with immediate visual updates.
* **Non-Modal Interface:** The settings panel is modeless, allowing users to interact with the main chart while tweaking configurations.
* **Decoupled Architecture:** Settings logic is injected via Functional Interfaces, removing hard dependencies between the UI components.

### 🎨 Modern UI
* **Dark Mode:** Fully themed using **FlatLaf** for a professional look and feel.

---

## 🛠️ Architecture & Design Patterns

The application is structured to strictly separate the View (UI), Model (Data), and Control logic.

### 1. Command Pattern
User interactions are encapsulated as objects, allowing for flexible command execution and easy extension.
* **`Command` Interface:** Defines the contract for all actions.
* **Implementations:**
    * `ExchangeMoneyCommand`: Handles the math and API calls for conversion.
    * `GetHistoricCommand`: Orchestrates fetching time-series data and rendering the chart.

### 2. Dependency Inversion & Functional Interfaces
To avoid circular dependencies between the `Desktop` (Main View) and the `SettingPanel`, this project utilizes **Java.util.function**:

* **`Consumer<Integer[]>` (The Setter):**
  The `SettingPanel` accepts a `Consumer`. When a toggle changes, it "feeds" the new state to this consumer. The `Desktop` provides the implementation (repainting the chart) without the Panel knowing about the Chart logic.

* **`Supplier<Integer[]>` (The Getter):**
  The `SettingPanel` accepts a `Supplier` to read the initial state of the application when opened.

### 3. External API Integration
* **Data Source:** [Frankfurter API](https://api.frankfurter.dev/)
* **JSON Parsing:** Uses **Gson** to parse complex JSON responses into Java Objects (`Currency`, `ExchangeRate`).

---

---

## 📂 Project Structure

```text
software.ulpgc.moneycalculator
├── application.Aguakate
│   ├── Desktop.java            
│   ├── Main.java              
│   ├── SettingPanel.java       
│   ├── ExchangeRateLoader.java 
│   └── WebService.java        
└── architecture
    ├── control                
    ├── io                     
    ├── model                 
    └── ui      
```
## 👥 Authors
- Jose Marcial Galván Franco

_Disclaimer: This README documentation was generated with the assistance of Artificial Intelligence._