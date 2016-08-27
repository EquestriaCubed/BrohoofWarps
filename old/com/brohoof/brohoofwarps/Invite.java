package com.brohoof.brohoofwarps;

import java.util.UUID;

public class Invite {
    private UUID invitee;

    public Invite(UUID invitee) {
        this.invitee = invitee;
    }

    public UUID getInvitee() {
        return invitee;
    }
}
