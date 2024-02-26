package server.handlers;

import service.AdminService;
import spark.Request;
import spark.Response;

public class ClearApplicationHandler {
    private AdminService adminService;

    public ClearApplicationHandler(AdminService adminService) {
        this.adminService = adminService;
    }

    public Object handle(Request req, Response res) throws Exception {
        try {
            adminService.clearApplication();
            res.status(200);
            return "";
        } catch(Exception e) {
            res.status(500);
            return e.getMessage();
        }
    }
}
