package de.ari24.packetlogger.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class PacketLoggerToast implements Toast {
    private final List<OrderedText> message;
    private final TextRenderer textRenderer;
    private final int width;

    public PacketLoggerToast(String message) {
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        this.width = Math.min(240, textRenderer.getWidth(message) + 16);
        this.message = this.wrap(List.of(Text.of(message)));
    }

    public static void notify(String message) {
        MinecraftClient.getInstance().getToastManager().add(new PacketLoggerToast(message));
    }

    @Override
    public Visibility draw(DrawContext context, ToastManager manager, long startTime) {
        final var width = this.getWidth();
        final var height = this.getHeight();
        context.fill(0, 0, width, height, 0x77000000);
        context.drawBorder(0, 0, this.getWidth(), this.getHeight(), 0xA800FBFF);
        final var textRenderer = this.textRenderer;
        int xOffset = this.getWidth() / 2 - textRenderer.getWidth(this.message.get(0)) / 2;
        context.drawText(textRenderer, this.message.get(0), xOffset, 4, 0xFFFFFF, true);
        for (int i = 1; i < this.message.size(); ++i)
            context.drawText(textRenderer, this.message.get(i), 4, i * 11, 0xFFFFFF, false);

        return startTime > 10000 ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public Object getType() {
        return Type.INFO;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return 6 + this.message.size() * 11;
    }

    private List<OrderedText> wrap(List<Text> message) {
        var list = new ArrayList<OrderedText>();
        for (var text : message) list.addAll(this.textRenderer.wrapLines(text, this.getWidth() - 8));
        return list;
    }

    enum Type {
        INFO,
    }
}
