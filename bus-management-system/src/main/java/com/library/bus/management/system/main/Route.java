/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.library.bus.management.system.main;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author HP
 */
public class Route {
    private int routeId;
    private String routeName;
    private String source;
    private String destination;
    private double distanceKm;
    private String routeType;
    private double baseFare;
    private Timestamp createdAt;
    private String createdBy;
    
    private static Connection connection = DatabaseConnection.getConnection();
    
    public Route() {}
    
    public Route(String routeName, String source, String destination, 
                double distanceKm, String routeType, double baseFare) {
        this.routeName = routeName;
        this.source = source;
        this.destination = destination;
        this.distanceKm = distanceKm;
        this.routeType = routeType;
        this.baseFare = baseFare;
        this.createdBy = "Hirusha suhan";
    }
    
    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }
    
    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    
    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
    
    public String getRouteType() { return routeType; }
    public void setRouteType(String routeType) { this.routeType = routeType; }
    
    public double getBaseFare() { return baseFare; }
    public void setBaseFare(double baseFare) { this.baseFare = baseFare; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public boolean save() {
        if (this.routeId == 0) {
            return addRoute();
        } else {
            return updateRoute();
        }
    }
    
    private boolean addRoute() {
        String sql = "INSERT INTO routes (route_name, source, destination, distance_km, route_type, base_fare, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, this.routeName);
            stmt.setString(2, this.source);
            stmt.setString(3, this.destination);
            stmt.setDouble(4, this.distanceKm);
            stmt.setString(5, this.routeType);
            stmt.setDouble(6, this.baseFare);
            stmt.setString(7, "hirushasuhan");
            
            int result = stmt.executeUpdate();
            if (result > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.routeId = rs.getInt(1);
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding route: " + e.getMessage());
        }
        return false;
    }
    
    private boolean updateRoute() {
        String sql = "UPDATE routes SET route_name=?, source=?, destination=?, distance_km=?, route_type=?, base_fare=? WHERE route_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, this.routeName);
            stmt.setString(2, this.source);
            stmt.setString(3, this.destination);
            stmt.setDouble(4, this.distanceKm);
            stmt.setString(5, this.routeType);
            stmt.setDouble(6, this.baseFare);
            stmt.setInt(7, this.routeId);
            
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating route: " + e.getMessage());
            return false;
        }
    }
    
    public boolean delete() {
        String sql = "DELETE FROM routes WHERE route_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, this.routeId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting route: " + e.getMessage());
            return false;
        }
    }
    
    public static List<Route> getAllRoutes() {
        List<Route> routes = new ArrayList<>();
        String sql = "SELECT * FROM routes ORDER BY route_id";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Route route = new Route();
                route.setRouteId(rs.getInt("route_id"));
                route.setRouteName(rs.getString("route_name"));
                route.setSource(rs.getString("source"));
                route.setDestination(rs.getString("destination"));
                route.setDistanceKm(rs.getDouble("distance_km"));
                route.setRouteType(rs.getString("route_type"));
                route.setBaseFare(rs.getDouble("base_fare"));
                route.setCreatedAt(rs.getTimestamp("created_at"));
                route.setCreatedBy(rs.getString("created_by"));
                routes.add(route);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving routes: " + e.getMessage());
        }
        return routes;
    }
    
    public static Route getRouteById(int routeId) {
        String sql = "SELECT * FROM routes WHERE route_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, routeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Route route = new Route();
                route.setRouteId(rs.getInt("route_id"));
                route.setRouteName(rs.getString("route_name"));
                route.setSource(rs.getString("source"));
                route.setDestination(rs.getString("destination"));
                route.setDistanceKm(rs.getDouble("distance_km"));
                route.setRouteType(rs.getString("route_type"));
                route.setBaseFare(rs.getDouble("base_fare"));
                route.setCreatedAt(rs.getTimestamp("created_at"));
                route.setCreatedBy(rs.getString("created_by"));
                return route;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving route: " + e.getMessage());
        }
        return null;
    }
    
    public static boolean deleteRouteById(int routeId) {
        String sql = "DELETE FROM routes WHERE route_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, routeId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting route: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Route{ID=%d, Name='%s', %s->%s, Distance=%.1fkm, Type='%s', Fare=Rs.%.2f}",
                routeId, routeName, source, destination, distanceKm, routeType, baseFare);
    }
}