public enum ClientRequests {
    DATETIME, UPTIME, MEMORYUSE, NETSTAT, CURRENTUSERS, RUNNINGPROCESSES;

    public static int getCommandCode(ClientRequests clientRequests) {
        switch (clientRequests) {
            case DATETIME:
                return 0;
            case UPTIME:
                return 1;
            case MEMORYUSE:
                return 2;
            case NETSTAT:
                return 3;
            case CURRENTUSERS:
                return 4;
            case RUNNINGPROCESSES:
                return 5;
            default:
                return -1;
        }
    }

    public static ClientRequests parseCommandCode(int code) {
        switch (code) {
            case 0:
                return DATETIME;
            case 1:
                return UPTIME;
            case 2:
                return MEMORYUSE;
            case 3:
                return NETSTAT;
            case 4:
                return CURRENTUSERS;
            case 5:
                return RUNNINGPROCESSES;
            default:
                return null;
        }
    }
}
