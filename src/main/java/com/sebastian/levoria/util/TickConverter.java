package com.sebastian.levoria.util;

public class TickConverter {
    /**
     * Converts Minecraft ticks into hours, minutes, and seconds.
     *
     * @param ticks The number of ticks to convert.
     * @return A string formatted as "hh:mm:ss".
     */
    public static String convertTicksToTime(int ticks) {
        // Minecraft ticks: 20 ticks = 1 second
        int totalSeconds = ticks / 20;

        // Calculate hours, minutes, and seconds
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        // Format as hh:mm:ss
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void main(String[] args) {
        // Example usage
        int exampleTicks = 123456; // Replace with your tick count
        String formattedTime = convertTicksToTime(exampleTicks);
        System.out.println("Time: " + formattedTime);
    }
}

