package messaging.store;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.Serializer;
import messaging.util.ObjRequest;
import util.RemoteObj;

/**
 * BuyRequest is just a distinguishable empty request to a cart.
 */
public class BuyRequest extends ObjRequest
{

    public BuyRequest(RemoteObj ro) {
        super(ro);
    }

    @Override
    public void writeObject(BufferOutput<?> buffer, Serializer serializer) {
        serializer.writeObject(getRemoteObj(), buffer);
    }

    @Override
    public void readObject(BufferInput<?> buffer, Serializer serializer) {
        setRemoteObj(serializer.readObject(buffer));
    }
}