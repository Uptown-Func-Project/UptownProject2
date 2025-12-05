package io.github.OMAL_Maze;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;

/**
 * Extension to initialize libGDX headless backend for tests.
 */
public class GdxTestRunner implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {
    private static HeadlessApplication application;
    private static boolean initialized = false;

    @Override
    public void beforeAll(ExtensionContext context) {
        if (!initialized) {
            initialized = true;
            
            HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
            
            application = new HeadlessApplication(new ApplicationListener() {
                @Override
                public void create() {}

                @Override
                public void resize(int width, int height) {}

                @Override
                public void render() {}

                @Override
                public void pause() {}

                @Override
                public void resume() {}

                @Override
                public void dispose() {}
            }, config);
            
            // Mock OpenGL since headless doesn't have graphics
            Gdx.gl20 = Mockito.mock(GL20.class);
            Gdx.gl = Gdx.gl20;
            
            // Register shutdown hook
            context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).put("gdx", this);
        }
    }

    @Override
    public void close() {
        if (application != null) {
            application.exit();
            application = null;
            initialized = false;
        }
    }
}
