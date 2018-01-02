package util;

import io.atomix.catalyst.transport.Connection;
import messaging.GetRemoteObjReply;
import messaging.GetRemoteObjRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * RemoteObjectStoreStub will cache the latest RemoteObj retrieved.
 * <p>
 * It is a class that fires off requests to a known object store
 * to store or retrieve unique-named objects.
 */
public class RemoteObjectStoreStub extends Stub implements RemoteObjectStore {
    private Map<String,RemoteObj> cached;


    public RemoteObjectStoreStub(RemoteObj b, Connection c) {
        super(b,c);
        cached = new HashMap<>();
    }

    @Override
    public Optional<RemoteObj> getObject(String name, long tag) {
        Optional<RemoteObj> ro;
        if(cached.containsKey(name)) {
            RemoteObj cache = cached.get(name);
            if(cache.getId()==tag) {
                ro = Optional.of(cache);
            }
            else {
                GetRemoteObjRequest rq = new GetRemoteObjRequest(getRef(),name,tag);
                ro = getConnection().<GetRemoteObjRequest, GetRemoteObjReply>sendAndReceive(rq)
                                    .join().getRemoteObj();
            }
        }
        else {
            GetRemoteObjRequest rq = new GetRemoteObjRequest(getRef(),name,tag);
            ro = getConnection().<GetRemoteObjRequest, GetRemoteObjReply>sendAndReceive(rq)
                                .join().getRemoteObj();
        }
        return ro;
    }

    @Override
    public Optional<RemoteObj> getObject(String name) {
        return Optional.empty();
    }

    @Override
    public boolean insertObject(String name, RemoteObj ro) {
        return false;
    }
}
