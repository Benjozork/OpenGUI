package me.benjozork.opengui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.benjozork.opengui.render.Backend;
import me.benjozork.opengui.render.backends.GDXBackend;
import me.benjozork.opengui.render.object.Font;
import me.benjozork.opengui.render.object.Keys;
import me.benjozork.opengui.render.object.TextComponent;
import me.benjozork.opengui.render.object.resource.InternalPath;
import me.benjozork.opengui.render.object.resource.Path;
import me.benjozork.opengui.serialization.loaders.FontDeserializer;
import me.benjozork.opengui.ui.Context;
import me.benjozork.opengui.ui.Element;
import me.benjozork.opengui.ui.Layout;
import me.benjozork.opengui.ui.Skin;
import me.benjozork.opengui.serialization.loaders.PathDeserializer;
import me.benjozork.opengui.serialization.loaders.ContextDeserializer;
import me.benjozork.opengui.serialization.loaders.ElementDeserializer;
import me.benjozork.opengui.serialization.loaders.LayoutDeserializer;
import me.benjozork.opengui.serialization.loaders.TextComponentDeserializer;

public class OpenGUI extends ApplicationAdapter {

	private static Gson gson;

	private static Context context;

	@Override
	public void create () {

		Backend backend = new GDXBackend();

		gson = new GsonBuilder()
				.registerTypeAdapter(Path.class, new PathDeserializer())
				.registerTypeAdapter(Context.class, new ContextDeserializer()).create();

		context = gson.fromJson(Gdx.files.internal("context.json").reader(), Context.class);

		gson = new GsonBuilder()
				.registerTypeAdapter(Path.class, new PathDeserializer())
				.registerTypeAdapter(Layout.class, new LayoutDeserializer(context))
				.registerTypeAdapter(TextComponent.class, new TextComponentDeserializer(context))
				.registerTypeAdapter(Element.class, new ElementDeserializer(context, null))
				.create();

		context.setBackend(backend);
		context.setRoot(gson.fromJson(context.getBackend().getReader(new InternalPath("layout.json")), Layout.class));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1 );
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (context.getBackend().isKeyDown(Keys.W)) {
            context.getRoot().getRoot().get(0).setRelX(context.getRoot().getRoot().get(0).getRelX() + 5);
        }

        context.getRoot().update();
		((GDXBackend) context.getBackend()).spriteBatch.begin();
		context.getRoot().draw();
		((GDXBackend) context.getBackend()).spriteBatch.end();
	}
	
	@Override
	public void dispose () {
		((GDXBackend) context.getBackend()).spriteBatch.dispose();
		((GDXBackend) context.getBackend()).shapeRenderer.dispose();
	}

	public static Gson getGson() {
		return gson;
	}

	public static Context getContext() {
		return context;
	}

	public static Backend getBackend() {
		return context.getBackend();
	}

}