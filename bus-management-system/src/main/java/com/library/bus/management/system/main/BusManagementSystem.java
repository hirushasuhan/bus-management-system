/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.library.bus.management.system.main;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author HP
 */
public class BusManagementSystem {
    private Scanner scanner;
    
    private static final String CURRENT_DATE = "2025-08-18";
    private static final String CURRENT_TIME = "06:39:50";
    private static final String CURRENT_DATETIME = "2025-08-18 06:39:50 UTC";
    private static final String CURRENT_USER = "Hirusha suhan";
    
    public BusManagementSystem() {
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        displayWelcomeScreen();
        
        if (!DatabaseConnection.testConnection()) {
            System.out.println("Cannot connect to database. Please check your MySQL setup.");
            System.out.println("Make sure you have run the database_schema.sql file first.");
            waitForEnter();
        }
        
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    routeManagementMenu();
                    break;
                case 2:
                    busManagementMenu();
                    break;
                case 3:
                    searchTimetable();
                    break;
                case 4:
                    fareCalculatorMenu();
                    break;
                case 5:
                    ReportService.generateCompleteReport();
                    waitForEnter();
                    break;
                case 6:
                    exitApplication();
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    waitForEnter();
            }
        }
    }
    
    private void displayWelcomeScreen() {
        clearScreen();
        System.out.println("=".repeat(70));
        System.out.println("                   BUS MANAGEMENT SYSTEM");
        System.out.println("=".repeat(70));
        System.out.println("Developed by: " + CURRENT_USER);
        System.out.println("Current Date: " + CURRENT_DATETIME);
        System.out.println("Database: bus_management_system_new");
        System.out.println("Java 8 Maven Application with MySQL Integration");
        System.out.println("=".repeat(70));
        
        System.out.println("\nInitializing system...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        waitForEnter();
    }
    
    private void displayMainMenu() {
        clearScreen();
        System.out.println("=".repeat(55));
        System.out.println("                    MAIN MENU");
        System.out.println("User: " + CURRENT_USER + " | " + CURRENT_DATETIME);
        System.out.println("=".repeat(55));
        System.out.println("1. Route Management");
        System.out.println("2. Bus Management");
        System.out.println("3. Search Bus Timetable");
        System.out.println("4. Fare Calculator");
        System.out.println("5. Generate Reports");
        System.out.println("6. Exit");
        System.out.println("=".repeat(55));
    }
    
    private void routeManagementMenu() {
        while (true) {
            clearScreen();
            System.out.println("=".repeat(45));
            System.out.println("             ROUTE MANAGEMENT");
            System.out.println("=".repeat(45));
            System.out.println("1. Add New Route");
            System.out.println("2. View All Routes");
            System.out.println("3. Update Route");
            System.out.println("4. Delete Route");
            System.out.println("5. Back to Main Menu");
            System.out.println("=".repeat(45));
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addNewRoute();
                    break;
                case 2:
                    viewAllRoutes();
                    break;
                case 3:
                    updateRoute();
                    break;
                case 4:
                    deleteRoute();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    waitForEnter();
            }
        }
    }
    
    private void addNewRoute() {
        clearScreen();
        System.out.println("ADD NEW ROUTE");
        System.out.println("=".repeat(50));
        System.out.println("Date: " + CURRENT_DATETIME);
        System.out.println("User: " + CURRENT_USER);
        System.out.println("-".repeat(50));
        
        System.out.print("Enter route name: ");
        String routeName = scanner.nextLine();
        
        System.out.print("Enter source: ");
        String source = scanner.nextLine();
        
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();
        
        double distance = getDoubleInput("Enter distance (km): ");
        
        System.out.println("\nSelect route type:");
        System.out.println("1. Normal");
        System.out.println("2. AC");
        System.out.println("3. Luxury");
        int typeChoice = getIntInput("Enter choice: ");
        
        String routeType;
        switch (typeChoice) {
            case 2: routeType = "ac"; break;
            case 3: routeType = "luxury"; break;
            default: routeType = "normal"; break;
        }
        
        double baseFare = getDoubleInput("Enter base fare (Rs.): ");
        
        Route route = new Route(routeName, source, destination, distance, routeType, baseFare);
        
        System.out.println("\nSaving route...");
        if (route.save()) {
            System.out.println("Route added successfully!");
            System.out.println("Route ID: " + route.getRouteId());
            System.out.println("Created: " + CURRENT_DATETIME);
            System.out.println("Created by: " + CURRENT_USER);
        } else {
            System.out.println("Failed to add route!");
        }
        
        waitForEnter();
    }
    
    private void viewAllRoutes() {
        clearScreen();
        System.out.println("ALL ROUTES");
        System.out.println("=".repeat(100));
        System.out.println("Generated: " + CURRENT_DATETIME + " | User: " + CURRENT_USER);
        System.out.println("-".repeat(100));
        
        List<Route> routes = Route.getAllRoutes();
        
        if (routes.isEmpty()) {
            System.out.println("No routes found.");
        } else {
            System.out.printf("%-5s %-25s %-15s %-15s %-10s %-10s %-12s%n",
                    "ID", "Route Name", "Source", "Destination", "Distance", "Type", "Base Fare");
            System.out.println("-".repeat(100));
            
            for (Route route : routes) {
                System.out.printf("%-5d %-25s %-15s %-15s %-10.1f %-10s Rs.%-7.2f%n",
                        route.getRouteId(), route.getRouteName(), route.getSource(),
                        route.getDestination(), route.getDistanceKm(), route.getRouteType().toUpperCase(), route.getBaseFare());
            }
            System.out.println("-".repeat(100));
            System.out.println("Total Routes: " + routes.size());
        }
        
        waitForEnter();
    }
    
    private void updateRoute() {
        clearScreen();
        System.out.println("UPDATE ROUTE");
        System.out.println("=".repeat(50));
        
        viewAllRoutesInline();
        
        int routeId = getIntInput("Enter Route ID to update: ");
        
        Route route = Route.getRouteById(routeId);
        if (route == null) {
            System.out.println("Route not found!");
            waitForEnter();
            return;
        }
        
        System.out.println("\nCurrent route details:");
        System.out.println(route);
        System.out.println("\nEnter new details (press Enter to keep current value):");
        
        System.out.print("Route name (" + route.getRouteName() + "): ");
        String input = scanner.nextLine();
        if (!input.trim().isEmpty()) {
            route.setRouteName(input);
        }
        
        System.out.print("Source (" + route.getSource() + "): ");
        input = scanner.nextLine();
        if (!input.trim().isEmpty()) {
            route.setSource(input);
        }
        
        System.out.print("Destination (" + route.getDestination() + "): ");
        input = scanner.nextLine();
        if (!input.trim().isEmpty()) {
            route.setDestination(input);
        }
        
        System.out.println("\nUpdating route...");
        if (route.save()) {
            System.out.println("Route updated successfully!");
            System.out.println("Updated: " + CURRENT_DATETIME);
            System.out.println("Updated by: " + CURRENT_USER);
        } else {
            System.out.println("Failed to update route!");
        }
        
        waitForEnter();
    }
    
    private void deleteRoute() {
        clearScreen();
        System.out.println("DELETE ROUTE");
        System.out.println("=".repeat(50));
        
        viewAllRoutesInline();
        
        int routeId = getIntInput("Enter Route ID to delete: ");
        
        Route route = Route.getRouteById(routeId);
        if (route == null) {
            System.out.println("Route not found!");
            waitForEnter();
            return;
        }
        
        System.out.println("\nRoute to be deleted:");
        System.out.println(route);
        System.out.print("\nAre you sure you want to delete this route? (y/N): ");
        String confirm = scanner.nextLine();
        
        if (confirm.toLowerCase().equals("y") || confirm.toLowerCase().equals("yes")) {
            System.out.println("Deleting route...");
            if (Route.deleteRouteById(routeId)) {
                System.out.println("Route deleted successfully!");
                System.out.println("Deleted: " + CURRENT_DATETIME);
                System.out.println("Deleted by: " + CURRENT_USER);
            } else {
                System.out.println("Failed to delete route!");
            }
        } else {
            System.out.println("Operation cancelled.");
        }
        
        waitForEnter();
    }
    
    private void busManagementMenu() {
        while (true) {
            clearScreen();
            System.out.println("=".repeat(45));
            System.out.println("             BUS MANAGEMENT");
            System.out.println("=".repeat(45));
            System.out.println("1. Add New Bus");
            System.out.println("2. View All Buses");
            System.out.println("3. Search Bus by ID");
            System.out.println("4. Search Bus by Number");
            System.out.println("5. Search Buses by Route ID");
            System.out.println("6. Update Bus");
            System.out.println("7. Delete Bus");
            System.out.println("8. Back to Main Menu");
            System.out.println("=".repeat(45));
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addNewBus();
                    break;
                case 2:
                    viewAllBuses();
                    break;
                case 3:
                    searchBusById();
                    break;
                case 4:
                    searchBusByNumber();
                    break;
                case 5:
                    searchBusesByRoute();
                    break;
                case 6:
                    updateBus();
                    break;
                case 7:
                    deleteBus();
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    waitForEnter();
            }
        }
    }
    
    private void addNewBus() {
        clearScreen();
        System.out.println("ADD NEW BUS");
        System.out.println("=".repeat(50));
        System.out.println("Date: " + CURRENT_DATETIME);
        System.out.println("User: " + CURRENT_USER);
        System.out.println("-".repeat(50));
        
        System.out.print("Enter bus number: ");
        String busNumber = scanner.nextLine();
        
        int capacity = getIntInput("Enter capacity: ");
        
        System.out.println("\nSelect bus type:");
        System.out.println("1. Normal");
        System.out.println("2. AC");
        System.out.println("3. Luxury");
        int typeChoice = getIntInput("Enter choice: ");
        
        String busType;
        switch (typeChoice) {
            case 2: busType = "ac"; break;
            case 3: busType = "luxury"; break;
            default: busType = "normal"; break;
        }
        
        System.out.print("Enter driver name: ");
        String driverName = scanner.nextLine();
        
        System.out.print("Enter driver phone: ");
        String driverPhone = scanner.nextLine();
        
        System.out.println("\nAvailable Routes:");
        viewAllRoutesInline();
        int routeId = getIntInput("Enter Route ID: ");
        
        Bus bus = new Bus(busNumber, capacity, busType, driverName, driverPhone, routeId);
        
        System.out.println("\nSaving bus...");
        if (bus.save()) {
            System.out.println("Bus added successfully!");
            System.out.println("Bus ID: " + bus.getBusId());
            System.out.println("Created: " + CURRENT_DATETIME);
            System.out.println("Created by: " + CURRENT_USER);
        } else {
            System.out.println("Failed to add bus! (Bus number might already exist)");
        }
        
        waitForEnter();
    }
    
    private void viewAllBuses() {
        clearScreen();
        System.out.println("ALL BUSES");
        System.out.println("=".repeat(110));
        System.out.println("Generated: " + CURRENT_DATETIME + " | User: " + CURRENT_USER);
        System.out.println("-".repeat(110));
        
        List<Bus> buses = Bus.getAllBuses();
        
        if (buses.isEmpty()) {
            System.out.println("No buses found.");
        } else {
            System.out.printf("%-5s %-12s %-8s %-10s %-20s %-15s %-8s %-12s%n",
                    "ID", "Bus Number", "Capacity", "Type", "Driver Name", "Phone", "Route", "Status");
            System.out.println("-".repeat(110));
            
            for (Bus bus : buses) {
                System.out.printf("%-5d %-12s %-8d %-10s %-20s %-15s %-8d %-12s%n",
                        bus.getBusId(), bus.getBusNumber(), bus.getCapacity(), bus.getBusType().toUpperCase(),
                        bus.getDriverName(), bus.getDriverPhone(), bus.getRouteId(), bus.getStatus().toUpperCase());
            }
            System.out.println("-".repeat(110));
            System.out.println("Total Buses: " + buses.size());
        }
        
        waitForEnter();
    }
    
    private void searchBusById() {
        clearScreen();
        System.out.println("SEARCH BUS BY ID");
        System.out.println("=".repeat(50));
        
        int busId = getIntInput("Enter Bus ID: ");
        Bus bus = Bus.getBusById(busId);
        
        if (bus != null) {
            System.out.println("\nBus found:");
            System.out.println("-".repeat(50));
            displayBusDetails(bus);
        } else {
            System.out.println("\nBus not found!");
        }
        
        waitForEnter();
    }
    
    private void searchBusByNumber() {
        clearScreen();
        System.out.println("SEARCH BUS BY NUMBER");
        System.out.println("=".repeat(50));
        
        System.out.print("Enter Bus Number: ");
        String busNumber = scanner.nextLine();
        Bus bus = Bus.getBusByNumber(busNumber);
        
        if (bus != null) {
            System.out.println("\nBus found:");
            System.out.println("-".repeat(50));
            displayBusDetails(bus);
        } else {
            System.out.println("\nBus not found!");
        }
        
        waitForEnter();
    }
    
    private void searchBusesByRoute() {
        clearScreen();
        System.out.println("SEARCH BUSES BY ROUTE");
        System.out.println("=".repeat(50));
        
        viewAllRoutesInline();
        
        int routeId = getIntInput("Enter Route ID: ");
        List<Bus> buses = Bus.getBusesByRouteId(routeId);
        
        if (buses.isEmpty()) {
            System.out.println("\nNo buses found for this route.");
        } else {
            System.out.println("\nBuses on Route " + routeId + ":");
            System.out.println("-".repeat(85));
            System.out.printf("%-5s %-12s %-8s %-10s %-20s %-15s %-10s%n",
                    "ID", "Bus Number", "Capacity", "Type", "Driver Name", "Phone", "Status");
            System.out.println("-".repeat(85));
            
            for (Bus bus : buses) {
                System.out.printf("%-5d %-12s %-8d %-10s %-20s %-15s %-10s%n",
                        bus.getBusId(), bus.getBusNumber(), bus.getCapacity(), bus.getBusType().toUpperCase(),
                        bus.getDriverName(), bus.getDriverPhone(), bus.getStatus().toUpperCase());
            }
            System.out.println("-".repeat(85));
            System.out.println("Total Buses: " + buses.size());
        }
        
        waitForEnter();
    }
    
    private void updateBus() {
        clearScreen();
        System.out.println("UPDATE BUS");
        System.out.println("=".repeat(50));
        
        viewAllBusesInline();
        
        int busId = getIntInput("Enter Bus ID to update: ");
        
        Bus bus = Bus.getBusById(busId);
        if (bus == null) {
            System.out.println("Bus not found!");
            waitForEnter();
            return;
        }
        
        System.out.println("\nCurrent bus details:");
        displayBusDetails(bus);
        System.out.println("\nEnter new details (press Enter to keep current value):");
        
        System.out.print("Driver name (" + bus.getDriverName() + "): ");
        String input = scanner.nextLine();
        if (!input.trim().isEmpty()) {
            bus.setDriverName(input);
        }
        
        System.out.print("Driver phone (" + bus.getDriverPhone() + "): ");
        input = scanner.nextLine();
        if (!input.trim().isEmpty()) {
            bus.setDriverPhone(input);
        }
        
        System.out.println("Status options: active, maintenance, inactive");
        System.out.print("Status (" + bus.getStatus() + "): ");
        input = scanner.nextLine();
        if (!input.trim().isEmpty()) {
            bus.setStatus(input);
        }
        
        System.out.println("\nUpdating bus...");
        if (bus.save()) {
            System.out.println("Bus updated successfully!");
            System.out.println("Updated: " + CURRENT_DATETIME);
            System.out.println("Updated by: " + CURRENT_USER);
        } else {
            System.out.println("Failed to update bus!");
        }
        
        waitForEnter();
    }
    
    private void deleteBus() {
        clearScreen();
        System.out.println("DELETE BUS");
        System.out.println("=".repeat(50));
        
        viewAllBusesInline();
        
        int busId = getIntInput("Enter Bus ID to delete: ");
        
        Bus bus = Bus.getBusById(busId);
        if (bus == null) {
            System.out.println("Bus not found!");
            waitForEnter();
            return;
        }
        
        System.out.println("\nBus to be deleted:");
        displayBusDetails(bus);
        System.out.print("\nAre you sure you want to delete this bus? (y/N): ");
        String confirm = scanner.nextLine();
        
        if (confirm.toLowerCase().equals("y") || confirm.toLowerCase().equals("yes")) {
            System.out.println("Deleting bus...");
            if (Bus.deleteBusById(busId)) {
                System.out.println("Bus deleted successfully!");
                System.out.println("Deleted: " + CURRENT_DATETIME);
                System.out.println("Deleted by: " + CURRENT_USER);
            } else {
                System.out.println("Failed to delete bus!");
            }
        } else {
            System.out.println("Operation cancelled.");
        }
        
        waitForEnter();
    }
    
    private void searchTimetable() {
        clearScreen();
        System.out.println("SEARCH BUS TIMETABLE");
        System.out.println("=".repeat(60));
        System.out.println("Search Date: " + CURRENT_DATETIME);
        System.out.println("User: " + CURRENT_USER);
        System.out.println("-".repeat(60));
        
        System.out.print("Enter source: ");
        String source = scanner.nextLine();
        
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();
        
        System.out.print("Enter date (YYYY-MM-DD) [Today: " + CURRENT_DATE + "]: ");
        String dateStr = scanner.nextLine();
        
        if (dateStr.trim().isEmpty()) {
            dateStr = CURRENT_DATE;
        }
        
        try {
            Date date = Date.valueOf(dateStr);
            System.out.println("\nSearching schedules...");
            List<BusSchedule> schedules = BusSchedule.searchSchedule(source, destination, date);
            
            if (schedules.isEmpty()) {
                System.out.println("\nNo buses found for the given criteria.");
                System.out.println("Source: " + source);
                System.out.println("Destination: " + destination);
                System.out.println("Date: " + dateStr);
            } else {
                System.out.println("\nAvailable Buses:");
                System.out.println("Search: " + source + " -> " + destination + " on " + dateStr);
                System.out.println("-".repeat(120));
                System.out.printf("%-5s %-12s %-35s %-12s %-12s %-8s %-8s%n",
                        "ID", "Bus Number", "Route", "Departure", "Arrival", "Type", "Seats");
                System.out.println("-".repeat(120));
                
                for (BusSchedule schedule : schedules) {
                    System.out.printf("%-5d %-12s %-35s %-12s %-12s %-8s %-8d%n",
                            schedule.getScheduleId(), schedule.getBusNumber(),
                            schedule.getSource() + "->" + schedule.getDestination(),
                            schedule.getDepartureTime(), schedule.getArrivalTime(),
                            schedule.getBusType().toUpperCase(), schedule.getAvailableSeats());
                }
                System.out.println("-".repeat(120));
                System.out.println("Total Results: " + schedules.size());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\nInvalid date format! Please use YYYY-MM-DD");
        }
        
        waitForEnter();
    }
    
    private void fareCalculatorMenu() {
        clearScreen();
        System.out.println("FARE CALCULATOR");
        System.out.println("=".repeat(70));
        System.out.println("Date: " + CURRENT_DATETIME);
        System.out.println("User: " + CURRENT_USER);
        System.out.println("-".repeat(70));
        
        System.out.println("Available Routes:");
        List<Route> routes = Route.getAllRoutes();
        
        if (routes.isEmpty()) {
            System.out.println("No routes available.");
            waitForEnter();
            return;
        }
        
        System.out.printf("%-5s %-35s %-30s %-10s%n", "ID", "Route Name", "Route", "Type");
        System.out.println("-".repeat(75));
        
        for (Route route : routes) {
            System.out.printf("%-5d %-35s %-30s %-10s%n", 
                    route.getRouteId(), route.getRouteName(), 
                    route.getSource() + " -> " + route.getDestination(),
                    route.getRouteType().toUpperCase());
        }
        
        int routeId = getIntInput("\nEnter Route ID for fare calculation: ");
        Route route = Route.getRouteById(routeId);
        
        if (route != null) {
            System.out.println("\nCalculating fare...");
            FareCalculator.displayFareBredown(route);
            
            int passengers = getIntInput("Enter number of passengers for booking summary: ");
            FareCalculator.displayBookingSummary(route, passengers);
        } else {
            System.out.println("\nRoute not found!");
        }
        
        waitForEnter();
    }
    
    private void exitApplication() {
        clearScreen();
        System.out.println("=".repeat(50));
        System.out.println("                   GOODBYE!");
        System.out.println();
        System.out.println("        Thank you for using");
        System.out.println("       Bus Management System");
        System.out.println();
        System.out.println("Session ended: " + CURRENT_DATETIME);
        System.out.println("User: " + CURRENT_USER);
        System.out.println();
        System.out.println("Developed by: hirushasuhan");
        System.out.println("=".repeat(50));
        
        DatabaseConnection.closeConnection();
        System.out.println("\nSystem shutdown complete.");
    }
    
    // Helper methods
    private void viewAllRoutesInline() {
        List<Route> routes = Route.getAllRoutes();
        if (!routes.isEmpty()) {
            System.out.printf("%-5s %-35s %-30s %-10s%n", "ID", "Route Name", "Route", "Type");
            System.out.println("-".repeat(75));
            for (Route route : routes) {
                System.out.printf("%-5d %-35s %-30s %-10s%n", 
                        route.getRouteId(), route.getRouteName(), 
                        route.getSource() + " -> " + route.getDestination(),
                        route.getRouteType().toUpperCase());
            }
            System.out.println();
        }
    }
    
    private void viewAllBusesInline() {
        List<Bus> buses = Bus.getAllBuses();
        if (!buses.isEmpty()) {
            System.out.printf("%-5s %-12s %-10s %-20s %-10s%n", "ID", "Bus Number", "Type", "Driver", "Status");
            System.out.println("-".repeat(60));
            for (Bus bus : buses) {
                System.out.printf("%-5d %-12s %-10s %-20s %-10s%n", 
                        bus.getBusId(), bus.getBusNumber(), 
                        bus.getBusType().toUpperCase(), bus.getDriverName(),
                        bus.getStatus().toUpperCase());
            }
            System.out.println();
        }
    }
    
    private void displayBusDetails(Bus bus) {
        System.out.println("Bus ID: " + bus.getBusId());
        System.out.println("Bus Number: " + bus.getBusNumber());
        System.out.println("Capacity: " + bus.getCapacity() + " seats");
        System.out.println("Type: " + bus.getBusType().toUpperCase());
        System.out.println("Driver: " + bus.getDriverName());
        System.out.println("Phone: " + bus.getDriverPhone());
        System.out.println("Route ID: " + bus.getRouteId());
        System.out.println("Status: " + bus.getStatus().toUpperCase());
    }
    
    private void waitForEnter() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void clearScreen() {
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
    
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Starting Bus Management System...");
        System.out.println("Current Date/Time: " + CURRENT_DATETIME);
        System.out.println("Current User: " + CURRENT_USER);
        System.out.println("Database: bus_management_system_new");
        
        BusManagementSystem system = new BusManagementSystem();
        system.start();
    }
}
