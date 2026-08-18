package logger;

public class JsonFormatter implements Formatter {
    @Override
    public String format(LogRecord record) {
        return "{"
                + "\"timestamp\":\"" + escape(record.getTimestamp().toString()) + "\","
                + "\"level\":\"" + record.getLevel() + "\","
                + "\"thread\":\"" + escape(record.getThreadName()) + "\","
                + "\"message\":\"" + escape(record.getMessage()) + "\""
                + "}";
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"':
                    result.append("\\\"");
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case '\t':
                    result.append("\\t");
                    break;
                default:
                    result.append(c);
            }
        }
        return result.toString();
    }
}

