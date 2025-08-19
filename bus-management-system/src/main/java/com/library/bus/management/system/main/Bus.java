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
public class Bus {
    private int busId;
    private String busNumber;
    private int capacity;
    private String busType;
    private String driverName;
    private String driverPhone;
    private int routeId;
    private String status;
    private Timestamp createdAt;
    private String createdBy;
    
    private static Connection connection = DatabaseConnection.getConnection();
    
    public Bus() {}
    
    public Bus(String busNumber, int capacity, String busType, 
               String driverName, String driverPhone, int routeId) {
        this.busNumber = busNumber;
        this.capacity = capacity;
        this.busType = busType;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.routeId = routeId;
        this.status = "active";
        this.createdBy = "hirushasuhan";
    }
    
    public int getBusId() { return busId; }
    public void setBusId(int busId) { this.busId = busId; }
    
    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public String getBusType() { return busType; }
    public void setBusType(String busType) { this.busType = busType; }
    
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    
    public String getDriverPhone() { return driverPhone; }
    public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }
    
    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public boolean save() {
        if (this.busId == 0) {
            return addBus();
        } else {
            return updateBus();
        }
    }
    
    private boolean addBus() {
        String sql = "INSERT INTO buses (bus_number, capacity, bus_type, driver_name, driver_phone, route_id, status, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, this.busNumber);
            stmt.setInt(2, this.capacity);
            stmt.setString(3, this.busType);
            stmt.setString(4, this.driverName);
            stmt.setString(5, this.driverPhone);
            stmt.setInt(6, this.routeId);
            stmt.setString(7, this.status);
            stmt.setString(8, "Hirusha suhan");
            
            int result = stmt.executeUpdate();
            if (result > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.busId = rs.getInt(1);
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding bus: " + e.getMessage());
        }
        return false;
    }
    
    private boolean updateBus() {
        String sql = "UPDATE buses SET bus_number=?, capacity=?, bus_type=?, driver_name=?, driver_phone=?, route_id=?, status=? WHERE bus_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, this.busNumber);
            stmt.setInt(2, this.capacity);
            stmt.setString(3, this.busType);
            stmt.setString(4, this.driverName);
            stmt.setString(5, this.driverPhone);
            stmt.setInt(6, this.routeId);
            stmt.setString(7, this.status);
            stmt.setInt(8, this.busId);
            
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating bus: " + e.getMessage());
            return false;
        }
    }
    
    public boolean delete() {
        String sql = "DELETE FROM buses WHERE bus_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, this.busId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting bus: " + e.getMessage());
            return false;
        }
    }
    
    public static List<Bus> getAllBuses() {
        List<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM buses ORDER BY bus_id";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Bus bus = new Bus();
                bus.setBusId(rs.getInt("bus_id"));
                bus.setBusNumber(rs.getString("bus_number"));
                bus.setCapacity(rs.getInt("capacity"));
                bus.setBusType(rs.getString("bus_type"));
                bus.setDriverName(rs.getString("driver_name"));
                bus.setDriverPhone(rs.getString("driver_phone"));
                bus.setRouteId(rs.getInt("route_id"));
                bus.setStatus(rs.getString("status"));
                bus.setCreatedAt(rs.getTimestamp("created_at"));
                bus.setCreatedBy(rs.getString("created_by"));
                buses.add(bus);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving buses: " + e.getMessage());
        }
        return buses;
    }
    
    public static Bus getBusById(int busId) {
        String sql = "SELECT * FROM buses WHERE bus_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, busId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Bus bus = new Bus();
                bus.setBusId(rs.getInt("bus_id"));
                bus.setBusNumber(rs.getString("bus_number"));
                bus.setCapacity(rs.getInt("capacity"));
                bus.setBusType(rs.getString("bus_type"));
                bus.setDriverName(rs.getString("driver_name"));
                bus.setDriverPhone(rs.getString("driver_phone"));
                bus.setRouteId(rs.getInt("route_id"));
                bus.setStatus(rs.getString("status"));
                bus.setCreatedAt(rs.getTimestamp("created_at"));
                bus.setCreatedBy(rs.getString("created_by"));
                return bus;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving bus: " + e.getMessage());
        }
        return null;
    }
    
    public static Bus getBusByNumber(String busNumber) {
        String sql = "SELECT * FROM buses WHERE bus_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, busNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Bus bus = new Bus();
                bus.setBusId(rs.getInt("bus_id"));
                bus.setBusNumber(rs.getString("bus_number"));
                bus.setCapacity(rs.getInt("capacity"));
                bus.setBusType(rs.getString("bus_type"));
                bus.setDriverName(rs.getString("driver_name"));
                bus.setDriverPhone(rs.getString("driver_phone"));
                bus.setRouteId(rs.getInt("route_id"));
                bus.setStatus(rs.getString("status"));
                bus.setCreatedAt(rs.getTimestamp("created_at"));
                bus.setCreatedBy(rs.getString("created_by"));
                return bus;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving bus by number: " + e.getMessage());
        }
        return null;
    }
    
    public static List<Bus> getBusesByRouteId(int routeId) {
        List<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM buses WHERE route_id = ? ORDER BY bus_number";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, routeId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Bus bus = new Bus();
                bus.setBusId(rs.getInt("bus_id"));
                bus.setBusNumber(rs.getString("bus_number"));
                bus.setCapacity(rs.getInt("capacity"));
                bus.setBusType(rs.getString("bus_type"));
                bus.setDriverName(rs.getString("driver_name"));
                bus.setDriverPhone(rs.getString("driver_phone"));
                bus.setRouteId(rs.getInt("route_id"));
                bus.setStatus(rs.getString("status"));
                bus.setCreatedAt(rs.getTimestamp("created_at"));
                bus.setCreatedBy(rs.getString("created_by"));
                buses.add(bus);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving buses by route: " + e.getMessage());
        }
        return buses;
    }
    
    public static boolean deleteBusById(int busId) {
        String sql = "DELETE FROM buses WHERE bus_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, busId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting bus: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Bus{ID=%d, Number='%s', Capacity=%d, Type='%s', Driver='%s', Phone='%s', RouteID=%d, Status='%s'}",
                busId, busNumber, capacity, busType, driverName, driverPhone, routeId, status);
    }
}

