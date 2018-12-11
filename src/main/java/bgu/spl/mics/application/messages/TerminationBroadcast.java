package bgu.spl.mics.application.messages;
import bgu.spl.mics.Broadcast;

/**
 * A "Marker" Broadcast, upon receiving this broadcast, each Micro-Service will
 * Terminate itself Gracefully
 */
public class TerminationBroadcast implements Broadcast {}
