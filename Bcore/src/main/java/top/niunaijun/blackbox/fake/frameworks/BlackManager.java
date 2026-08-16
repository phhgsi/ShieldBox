package top.niunaijun.blackbox.fake.frameworks;

import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.ParameterizedType;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.utils.Reflector;

/**
 * Created by BlackBox on 2022/3/23.
 */
public abstract class BlackManager<Service extends IInterface> {
    public static final String TAG = "BlackManager";

    private Service mService;

    protected abstract String getServiceName();

    public Service getService() {
        if (mService != null && mService.asBinder().pingBinder() && mService.asBinder().isBinderAlive()) {
            return mService;
        }
        try {
            IBinder binder = BlackBoxCore.get().getService(getServiceName());
            if (binder == null) {
                return null;
            }
            mService = Reflector.on(getTClass().getName() + "$Stub").method("asInterface", IBinder.class)
                    .call(binder);
            if (mService != null && mService.asBinder() != null) {
                mService.asBinder().linkToDeath(new IBinder.DeathRecipient() {
                    @Override
                    public void binderDied() {
                        if (mService != null && mService.asBinder() != null) {
                            mService.asBinder().unlinkToDeath(this, 0);
                        }
                        mService = null;
                    }
                }, 0);
            }
            return mService;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    private Class<Service> getTClass() {
        return (Class<Service>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
