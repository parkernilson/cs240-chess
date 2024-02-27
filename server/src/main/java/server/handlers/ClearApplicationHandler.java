package server.handlers;

import java.util.Map;

import com.google.gson.Gson;

import service.AdminService;
import spark.Request;
import spark.Response;

public class ClearApplicationHandler {
    private AdminService adminService;

    public ClearApplicationHandler(AdminService adminService) {
        this.adminService = adminService;
    }

    public Object handle(Request req, Response res) {
        try {
            adminService.clearApplication();
            res.status(200);
            return "";
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", "Error: Internal server error"));
        }
    }
}
