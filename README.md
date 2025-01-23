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


## App Screenshots

### Splash Screen, Expenses Screen, Add Income/Expense
**Splash Screen**  
The welcome screen when the app starts.  
![Splash Screen](Screenshot_20230808_034211.jpg)

**Expenses Screen**  
Displays the list of expense transactions with options to add or edit.  
![Expenses Screen](Screenshot_20230808_035454.jpg)

**Add Income/Expense**  
Allows the user to add new income or expense records.  
![Add Income/Expense](Screenshot_20250123_125715.jpg)

---

### Add Expense, Add Income/Expense Category, Home Screen
**Edit**  
Interface to edit an expense or income, including selecting categories and entering amounts.  
![Edit Screen](Screenshot_20230808_034315.jpg)

**Add Income/Expense Category**  
Allows users to add custom categories for income and expenses.  
![Add Income/Expense Category](Screenshot_20250123_125656.jpg)

**Home Screen**  
Displays the balance, total income, and expenses, along with options to add new transactions.  
![Home Screen](Screenshot_20230808_034215.jpg)



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
