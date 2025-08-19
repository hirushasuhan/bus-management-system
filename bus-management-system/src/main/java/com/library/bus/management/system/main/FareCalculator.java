/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.library.bus.management.system.main;

/**
 *
 * @author HP
 */
public class FareCalculator {
    
    public static double calculateFare(Route route) {
        return calculateFare(route.getDistanceKm(), route.getRouteType(), route.getBaseFare());
    }
    
    public static double calculateFare(double distance, String busType, double baseFare) {
        double fare = baseFare;
        
        if (distance > 100) {
            double extraDistance = distance - 100;
            fare += extraDistance * 2.5;
        } else if (distance > 50) {
            double extraDistance = distance - 50;
            fare += extraDistance * 1.5;
        }
        
        double typeMultiplier = getBusTypeMultiplier(busType);
        fare *= typeMultiplier;
        
        fare += fare * 0.05; // 5% weekend Sunday
        
        return Math.round(fare * 100.0) / 100.0;
    }
    
    private static double getBusTypeMultiplier(String busType) {
        switch (busType.toLowerCase()) {
            case "luxury":
                return 1.6;
            case "ac":
                return 1.35;
            case "normal":
            default:
                return 1.0;
        }
    }
    
    public static void displayFareBredown(Route route) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("               FARE CALCULATION BREAKDOWN");
        System.out.println("              Generated: 2025-08-18 06:26:08 UTC");
        System.out.println("              By: hirushasuhan");
        System.out.println("=".repeat(60));
        System.out.println("Route: " + route.getRouteName());
        System.out.println("Journey: " + route.getSource() + " -> " + route.getDestination());
        System.out.println("Distance: " + route.getDistanceKm() + " km");
        System.out.println("Bus Type: " + route.getRouteType().toUpperCase());
        System.out.println("Base Fare: Rs. " + String.format("%.2f", route.getBaseFare()));
        System.out.println("Date: Sunday, 2025-08-18 (Weekend)");
        
        double distance = route.getDistanceKm();
        double baseFare = route.getBaseFare();
        String busType = route.getRouteType();
        
        System.out.println("\nCalculation Steps:");
        System.out.println("1. Base Fare: Rs. " + String.format("%.2f", baseFare));
        
        double distanceSurcharge = 0;
        if (distance > 100) {
            distanceSurcharge = (distance - 100) * 2.5;
            System.out.printf("2. Distance Surcharge (%.1f km > 100): Rs. %.2f%n", (distance - 100), distanceSurcharge);
        } else if (distance > 50) {
            distanceSurcharge = (distance - 50) * 1.5;
            System.out.printf("2. Distance Surcharge (%.1f km > 50): Rs. %.2f%n", (distance - 50), distanceSurcharge);
        } else {
            System.out.println("2. Distance Surcharge: Rs. 0.00 (under 50km)");
        }
        
        double subtotal = baseFare + distanceSurcharge;
        System.out.println("3. Subtotal: Rs. " + String.format("%.2f", subtotal));
        
        double multiplier = getBusTypeMultiplier(busType);
        System.out.println("4. Bus Type Multiplier (" + busType + "): " + multiplier + "x");
        
        double afterTypeMultiplier = subtotal * multiplier;
        System.out.println("5. After Type Multiplier: Rs. " + String.format("%.2f", afterTypeMultiplier));
        
        double weekendSurcharge = afterTypeMultiplier * 0.05;
        System.out.println("6. Sunday Surcharge (5%): Rs. " + String.format("%.2f", weekendSurcharge));
        
        double finalFare = calculateFare(route);
        System.out.println("\n" + "-".repeat(60));
        System.out.println("TOTAL FARE: Rs. " + String.format("%.2f", finalFare));
        System.out.println("-".repeat(60));
        
        System.out.println("\nComparison with other bus types:");
        double normalFare = calculateFare(distance, "normal", baseFare);
        double acFare = calculateFare(distance, "ac", baseFare);
        double luxuryFare = calculateFare(distance, "luxury", baseFare);
        
        System.out.printf("Normal Bus:  Rs. %8.2f%n", normalFare);
        System.out.printf("AC Bus:      Rs. %8.2f%n", acFare);
        System.out.printf("Luxury Bus:  Rs. %8.2f%n", luxuryFare);
        
        double savings = luxuryFare - normalFare;
        System.out.printf("Luxury Premium: Rs. %8.2f (%.1f%% more than Normal)%n", 
                savings, (savings/normalFare)*100);
        
        System.out.println("=".repeat(60));
        System.out.println("Note: Prices include Sunday surcharge");
        System.out.println("Valid for: 2025-08-18");
        System.out.println("=".repeat(60) + "\n");
    }
    
    public static double calculateTotalFare(Route route, int passengerCount) {
        double singleFare = calculateFare(route);
        double totalFare = singleFare * passengerCount;
        
        if (passengerCount > 5) {
            totalFare *= 0.95;
            System.out.println("Group discount applied (5% off for " + passengerCount + " passengers)");
        }
        
        return Math.round(totalFare * 100.0) / 100.0;
    }
    
    public static void displayBookingSummary(Route route, int passengers) {
        System.out.println("\nBOOKING SUMMARY");
        System.out.println("=".repeat(40));
        System.out.println("Route: " + route.getRouteName());
        System.out.println("Passengers: " + passengers);
        System.out.println("Single Fare: Rs. " + String.format("%.2f", calculateFare(route)));
        System.out.println("Total Fare: Rs. " + String.format("%.2f", calculateTotalFare(route, passengers)));
        System.out.println("Date: 2025-08-18 06:26:08 UTC");
        System.out.println("Booked by: Hirusha suhan");
        System.out.println("=".repeat(40) + "\n");
    }
}