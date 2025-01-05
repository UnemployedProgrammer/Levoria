package com.sebastian.levoria.client_cfg;

public class ClientConfig {
    public static ClientConfig INSTANCE = ClientConfig.defaultSettings();
    public static ClientConfig defaultSettings() {
        return new ClientConfig(true, 60, false);
    }

    private boolean hideExperimentalWarning, acceptDebugRendererPackets;
    private int hideExperimentalWarningDelay;

    public ClientConfig(boolean hideExperimentalWarning, int hideExperimentalWarningDelay, boolean acceptDebugRendererPackets) {
        this.hideExperimentalWarning = hideExperimentalWarning;
        this.hideExperimentalWarningDelay = hideExperimentalWarningDelay;
    }

    public ClientConfig(boolean hideExperimentalWarning, double hideExperimentalWarningDelay, boolean acceptDebugRendererPackets) {
        this.hideExperimentalWarning = hideExperimentalWarning;
        this.hideExperimentalWarningDelay = (int) Math.round(hideExperimentalWarningDelay);
    }

    public boolean isHideExperimentalWarning() {
        return hideExperimentalWarning;
    }

    public void setHideExperimentalWarning(boolean hideExperimentalWarning) {
        this.hideExperimentalWarning = hideExperimentalWarning;
    }

    public int getHideExperimentalWarningDelay() {
        return hideExperimentalWarningDelay;
    }

    public void setHideExperimentalWarningDelay(int hideExperimentalWarningDelay) {
        this.hideExperimentalWarningDelay = hideExperimentalWarningDelay;
    }

    public void setAcceptDebugRendererPackets(boolean acceptDebugRendererPackets) {
        this.acceptDebugRendererPackets = acceptDebugRendererPackets;
    }

    public boolean isAcceptDebugRendererPackets() {
        return acceptDebugRendererPackets;
    }
}
