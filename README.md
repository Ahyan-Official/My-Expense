# My Expense App

This is an Android application built with Java using SQLite as the database. The app is designed to help users track and manage their income and expenses. It supports multiple categories for both income and expenses, allowing users to easily categorize, edit, and delete their financial transactions.

## Features

- **Expense Tracking**: Add, view, edit, and delete expenses categorized by type (e.g., shopping, bills, transportation, etc.).
- **Income Tracking**: Add and manage various sources of income such as salary, profit, gifts, etc.
- **Balance Calculation**: Displays a balance based on the difference between total income and total expenses.
- **Fragments & Activities**: The app is structured with multiple fragments for better user experience and navigation. Each category (income and expense) has its own fragment.
- **SQLite Database**: Stores all expense and income records locally in a SQLite database for persistent storage.

## Technologies Used

- **Android Studio**: Integrated Development Environment (IDE) for building the app.
- **Java**: The main programming language used to build the app.
- **SQLite**: The local database for storing income and expense data.
- **Fragments**: Used to manage different sections of the app, such as the home screen and categories.
- **Material Design**: For a clean and user-friendly UI/UX.

## App Screenshots

### Home Screen, Expense Editing, Category Expenses

| ![Home Screen](https://via.placeholder.com/150) | ![Expense Editing](https://via.placeholder.com/150) | ![Category Expenses](https://via.placeholder.com/150) |
| --- | --- | --- |
| **Home Screen**: Displays the balance, total income, and expenses, along with options to add new transactions. | **Expense Editing**: The app allows users to edit existing expense records, including changing the category and amount. | **Category Expenses**: The app organizes expenses into categories like "Bills," "Shopping," and "Health." |

### Splash Screen, Income Categories, Main Screen

| ![Splash Screen](Screenshot_20230808_034211.jpg) | ![Income Categories](https://via.placeholder.com/150) | ![Main Screen](https://via.placeholder.com/150) |
| --- | --- | --- |
| **Splash Screen**: The welcome screen when the app starts. | **Income Categories**: Allows users to track different sources of income like salary, profit, and gifts. | **Main Screen**: Displays the balance and detailed transactions. |

## Class Structure

Here is a list of the main classes in the app:

- **DataHelper**: Handles all database operations, such as inserting, updating, and deleting records.
- **ExpenseFragment**: Manages the user interface and logic for the expense-related functionality.
- **HomeFragment**: Displays the balance and income/expense summary.
- **IncomeFragment**: Handles the logic and UI for income-related transactions.
- **MainActivity**: The entry point for the app, which handles navigation between fragments.
- **Splashscreen**: Displays a loading or welcome screen on startup.
- **ViewPagerAdapter**: Used for managing page transitions between different views in the app.

## How to Run the App

1. Clone the repository:
   ```bash
   git clone https://github.com/Ahyan-Official/My-Expense.git
