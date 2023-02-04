package org.lexize.ldocs.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.List;

public class LDocsDrawHelper {
    private static Minecraft getClient() {
        return Minecraft.getInstance();
    }
    public static void fill(PoseStack matrices, float x, float y, float width, float height, int rgba) {
        float a = (rgba & 0xFF) / 255f;
        float r = (rgba >> 24 & 0xFF) / 255f;
        float g = (rgba >> 16 & 0xFF) / 255f;
        float b = (rgba >> 8 & 0xFF) / 255f;
        fill(matrices, x, y, width, height, r,g,b,a);
    }
    public static void fill(PoseStack matrices, float x, float y, float width, float height, float r, float g, float b, float a) {
        float x1 = Math.min(x, x+width);
        float x2 = Math.max(x, x+width);
        float y1 = Math.min(y, y+height);
        float y2 = Math.max(y, y+height);
        var matrix = matrices.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bb.vertex(matrix, x1, y2, 0.0F).color(r,g,b,a).endVertex();
        bb.vertex(matrix, x2, y2, 0.0F).color(r,g,b,a).endVertex();
        bb.vertex(matrix, x2, y1, 0.0F).color(r,g,b,a).endVertex();
        bb.vertex(matrix, x1, y1, 0.0F).color(r,g,b,a).endVertex();
        BufferUploader.drawWithShader(bb.end());
    }
    public static void fillImage(PoseStack matrices, float x, float y, float width, float height, float u1, float v1, float u2, float v2, int rgba) {
        float a = (rgba & 0xFF) / 255f;
        float r = (rgba >> 24 & 0xFF) / 255f;
        float g = (rgba >> 16 & 0xFF) / 255f;
        float b = (rgba >> 8 & 0xFF) / 255f;
        fillImage(matrices, x, y, width, height, u1,v1,u2,v2, r,g,b,a);
    }
    public static void fillImage(PoseStack matrices, float x, float y, float width, float height, float u1, float v1, float u2, float v2, float r, float g, float b, float a) {
        float x1 = Math.min(x, x+width);
        float x2 = Math.max(x, x+width);
        float y1 = Math.min(y, y+height);
        float y2 = Math.max(y, y+height);
        var matrix = matrices.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bb.vertex(matrix, x1, y2, 0.0F).color(r,g,b,a).uv(u1,v2).endVertex();
        bb.vertex(matrix, x2, y2, 0.0F).color(r,g,b,a).uv(u2,v2).endVertex();
        bb.vertex(matrix, x2, y1, 0.0F).color(r,g,b,a).uv(u2,v1).endVertex();
        bb.vertex(matrix, x1, y1, 0.0F).color(r,g,b,a).uv(u1,v1).endVertex();
        BufferUploader.drawWithShader(bb.end());
    }
    public static void fillRotated(PoseStack matrices, float x, float y, float width, float height, float angle, int rgba) {
        float a = (rgba & 0xFF) / 255f;
        float r = (rgba >> 24 & 0xFF) / 255f;
        float g = (rgba >> 16 & 0xFF) / 255f;
        float b = (rgba >> 8 & 0xFF) / 255f;
        fillRotated(matrices, x, y, width, height, angle, r,g,b,a);
    }
    public static void fillRotated(PoseStack matrices, float x, float y, float width, float height, float angle, float r, float g, float b, float a) {
        matrices.pushPose();
        matrices.mulPoseMatrix(new Matrix4f().rotate(angle,0,0,1).setTranslation(width/2,height/2,0));
        matrices.translate(x,y,0);
        fill(matrices, x, y, width, height, r,g,b,a);
        matrices.popPose();
    }
    public static void fillImageRotated(PoseStack matrices, float x, float y, float width, float height, float angle, float u1, float v1, float u2, float v2, int rgba) {
        float a = (rgba & 0xFF) / 255f;
        float r = (rgba >> 24 & 0xFF) / 255f;
        float g = (rgba >> 16 & 0xFF) / 255f;
        float b = (rgba >> 8 & 0xFF) / 255f;
        fillImageRotated(matrices, x, y, width, height, angle, u1, v1, u2, v2, r,g,b,a);
    }

    public static void fillImageRotated(PoseStack matrices, float x, float y, float width, float height, float angle, float u1, float v1, float u2, float v2, float r, float g, float b, float a) {
        matrices.pushPose();
        matrices.mulPoseMatrix(new Matrix4f().rotate(angle,0,0,1).setTranslation(width/2,height/2,0));
        matrices.translate(x,y,0);
        fillImage(matrices, -(width/2), -(height/2), width, height, u1, v1, u2, v2, r,g,b,a);
        matrices.popPose();
    }
    public static void drawText(PoseStack poseStack, Component text, float x, float y, float z, float size, int color) {
        poseStack.pushPose();
        poseStack.translate(x,y,z);
        poseStack.scale(size,size,size);
        getClient().font.draw(poseStack, text, 0,0, color);
        poseStack.popPose();
    }
    public static void drawText(PoseStack poseStack, FormattedCharSequence text, float x, float y, float z, float size, int color) {
        poseStack.pushPose();
        poseStack.translate(x,y,z);
        poseStack.scale(size,size,size);
        getClient().font.draw(poseStack, text, 0,0, color);
        poseStack.popPose();
    }
    public static List<FormattedCharSequence> wrapText(Component text, int maxWidth, float textSize) {
        return getClient().font.split(text, (int)(maxWidth/textSize));
    }
}
