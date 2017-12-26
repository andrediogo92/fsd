package util;

import io.atomix.catalyst.transport.Address;
import store.Book;
import store.Cart;
import store.Store;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Class responsible for figuring out object type, generating references
 * and inserting the objects into the exported objects store.
 *
 *
 * @author André Diogo
 * @author Diogo Pimenta
 * @version 1.2, 22-12-2017
 * @see RemoteObj
 * @see DistObjManager
 */
final class RemoteObjFactory {
    private Address address;
    private Map <String,Map<Long,Object>> objstr;
    private AtomicLong tag;

    /**
     * RemoteObjFactory needs to know the address or index(for Clique) in which it is
     * operating to assign to references it generates.
     * Needs the underlying store reference to populate with exported objects.
     * @param address The network address of the process's DistObjManager.
     * @param objstr The object store of the DistObjManager.
     * @see DistObjManager
     */
    RemoteObjFactory(Address address, Map<String, Map<Long, Object>> objstr) {
        this.address = address;
        this.objstr = objstr;
        this.tag = new AtomicLong(0);
    }

    /**
     * Imports a stub from a given remote object.
     * <p>
     * Stubs are infused with the calling processes network info,
     * in order to delegate actual method invocations through RMI
     * transparently.
     * @param b The object reference
     * @return A stub or empty(in case of invalid class -> should never happen)
     * @see RemoteObj
     */
    protected Optional<Object> importRef(RemoteObj b) {
        String cls = b.getCls();
        if (cls.equals(Book.class.getName())) {
            return Optional.of(new BookStub(b));
        }
        else if(cls.equals(Cart.class.getName())) {
            return Optional.of(new CartStub(b));
        }
        else if(cls.equals(Store.class.getName())) {
            return Optional.of(new StoreStub(b));
        }
        else if(cls.equals(ObjectStore.class.getName())) {
            return Optional.of(new ObjectStoreStub(b));
        }
        return Optional.empty();
    }

    /**
     * Exports a reference from a given remote object.
     * @param b The object to export
     * @return A reference or empty(in case of invalid class/null-parameter -> caller ensures it never happens)
     * @see RemoteObj
     */
    protected Optional<RemoteObj> exportRef(Object b) {
        if(b!=null) {
            Map<Long,Object> mp = null;
            String cls;
            if (b instanceof Book) {
                cls = Book.class.getName();
            }
            else if (b instanceof Cart) {
                cls = Cart.class.getName();
            }
            else if (b instanceof ObjectStore) {
                cls = ObjectStore.class.getName();
            }
            else if (b instanceof Store) {
                cls = Store.class.getName();
            }
            else {
                return Optional.empty();
            }
            if(!objstr.containsKey(cls)) {
                objstr.put(cls,new HashMap<>());
                mp = objstr.get(cls);
            }
            long id = tag.getAndIncrement();
            mp.put(id,b);
            return Optional.of(new RemoteObj(address,id,cls));
        }
        return Optional.empty();
    }
}