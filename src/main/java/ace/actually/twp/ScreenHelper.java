package ace.actually.twp;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

public class ScreenHelper {


    public static int RGBA(int r, int g, int b, int a) {
        return (a << 24) | ((r & 255) << 16) | ((g & 255) << 8) | (b & 255);
    }

    public static void line(MatrixStack matrices, int lx1, int ly1, int lx2, int ly2)
    {
        if(ly2>=ly1)
        {
            if(ly2==ly1)
            {
                horizontal(matrices,lx1,lx2,ly2,RGBA(255,255,255,255));
            }
            else
            {
                polygon(matrices,lx2-2,ly2,lx2,ly2,lx1,ly1,lx1-2,ly1,RGBA(255,255,255,255));
            }
            return;
        }
        polygon(matrices,lx1-2,ly1,lx1,ly1,lx2,ly2,lx2-2,ly2,RGBA(255,255,255,255));

    }

    private static void polygon(MatrixStack matrixStack, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int color) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();


        float f = (float)(color >> 24 & 255) / 255.0F;
        float g = (float)(color >> 16 & 255) / 255.0F;
        float h = (float)(color >> 8 & 255) / 255.0F;
        float j = (float)(color & 255) / 255.0F;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        //bl,br,tr,tl
        bufferBuilder.vertex(matrix, (float)x1, (float)y1, 0.0F).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix, (float)x2, (float)y2, 0.0F).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix, (float)x3, (float)y3, 0.0F).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix, (float)x4, (float)y4, 0.0F).color(g, h, j, f).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    private static void horizontal(MatrixStack matrices, int x1, int x2, int y, int color) {
        if (x2 < x1) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }

        DrawableHelper.fill(matrices, x1, y, x2 + 1, y + 1, color);
    }
}
