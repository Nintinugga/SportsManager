package com.comp.ninti.general;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SerializableService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SAVE_SERIALIZABLE = "com.comp.ninti.general.action.SAVE_SERIALIZABLE";
    private static final String ACTION_READ_SERIALIZABLE = "com.comp.ninti.general.action.READ_SERIALIZABLE";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.comp.ninti.general.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.comp.ninti.general.extra.PARAM2";

    public SerializableService() {
        super("SerializableService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSaveSerializable(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SerializableService.class);
        intent.setAction(ACTION_SAVE_SERIALIZABLE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionReadSerializable(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SerializableService.class);
        intent.setAction(ACTION_READ_SERIALIZABLE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SAVE_SERIALIZABLE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionSaveSerializable(param1, param2);
            } else if (ACTION_READ_SERIALIZABLE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionReadSerializable(param1, param2);
            }
        }
    }


    private void handleActionSaveSerializable(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private void handleActionReadSerializable(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
