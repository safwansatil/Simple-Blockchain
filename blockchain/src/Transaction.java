
import java.lang.reflect.Array;
import java.security.*;
import java.util.ArrayList;

public class Transaction {
    public String transactionId;
    public PublicKey sender;
    public PublicKey reciepient;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0; // a rough count of how many transactions have been generated


    public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}


    private String calulateHash() {
		sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
		return StringUtil.applySha256(
				StringUtil.getStringFromKey(sender) +
				StringUtil.getStringFromKey(reciepient) +
				Float.toString(value) + sequence
				);
	} // transactionHash used as Id

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
        signature = StringUtil.applyECDSASig(privateKey,data);		
    }

    public boolean verifiySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    //Returns true if new transaction could be created.	
public boolean processTransaction() {
		
    if(verifiySignature() == false) {
        System.out.println("#Transaction Signature failed to verify");
        return false;
    }
            
    //gather transaction inputs (Make sure they are unspent):
    for(TransactionInput i : inputs) {
        i.UTXO = App.UTXOs.get(i.transactionOutputId);
    }

    //check if transaction is valid:
    if(getInputsValue() < App.minimumTransaction) {
        System.out.println("#Transaction Inputs to small: " + getInputsValue());
        return false;
    }
    
    //generate transaction outputs:
    float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
    transactionId = calulateHash();
    outputs.add(new TransactionOutput( this.reciepient, value,transactionId)); //send value to recipient
    outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender		
            
    //add outputs to Unspent list
    for(TransactionOutput o : outputs) {
        App.UTXOs.put(o.id , o);
    }
    
    //remove transaction inputs from UTXO lists as spent:
    for(TransactionInput i : inputs) {
        if(i.UTXO == null) continue; //if Transaction can't be found skip it 
        App.UTXOs.remove(i.UTXO.id);
    }
    
    return true;
}

//returns sum of inputs(UTXOs) values
public float getInputsValue() {
    float total = 0;
    for(TransactionInput i : inputs) {
        if(i.UTXO == null) continue; //if Transaction can't be found skip it 
        total += i.UTXO.value;
    }
    return total;
}

//returns sum of outputs:
public float getOutputsValue() {
    float total = 0;
    for(TransactionOutput o : outputs) {
        total += o.value;
    }
    return total;
}





}
