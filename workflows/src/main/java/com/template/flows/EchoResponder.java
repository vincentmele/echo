package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatedBy;
import net.corda.core.utilities.UntrustworthyData;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(EchoInitiator.class)
public class EchoResponder extends FlowLogic<Void> {
    private FlowSession counterpartySession;

    public EchoResponder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        // Responder flow logic goes here.
        UntrustworthyData<String> receivedData = counterpartySession.receive(String.class);
        String receivedStr = receivedData.unwrap(data -> {return data;});
        System.out.println(receivedStr);
        StringBuilder sb = new StringBuilder();
        sb.append(receivedStr);
        sb.reverse();

        counterpartySession.send(sb.toString());
        return null;
    }
}
