/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kma.librarysmartcard;


import java.util.List;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.JOptionPane;

/**
 *
 * @author admin
 */
public class ConnectSmardCard {
    public static final byte[] AID_APPLET = {(byte)0x11,(byte)0x22,(byte)0x33,(byte)0x44,(byte)0x55,(byte)0x00};
    private Card card;
    private TerminalFactory factory;
    private CardChannel channel;
    private CardTerminal terminal;
    private List<CardTerminal> terminals;
    private ResponseAPDU response;
    
    public ConnectSmardCard() {
        
    }
    
    public static void main(String[] args) {
        
    }
    
    public boolean connectCard() {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=0");
            channel = card.getBasicChannel();
            if(channel == null) {
                return false;
            }
            response = channel.transmit(new CommandAPDU(0x00, (byte)0xA4,0x04, 0x00, AID_APPLET));
            String check = Integer.toHexString(response.getSW());
            if(check.equals("9000")) {
                return true;
            }else if(check.equals("6400")) {
                JOptionPane.showMessageDialog(null, "the vo hieu hoa");
                return true;
            }else {
                return false;
            }
            
        }catch(Exception e){}
        return false;
    }
    
    public boolean disconnect() {
        try {
            card.disconnect(false);
            return true;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
