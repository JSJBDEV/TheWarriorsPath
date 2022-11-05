package ace.actually.twp.buttons;

import ace.actually.twp.TWP;
import ace.actually.twp.screens.StatScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnimatedButtonWidget extends ButtonWidget {
    int ticks;
    int count;

    boolean complete;
    boolean disabled;

    List<AnimatedButtonWidget> preconditions;

    private final List<Identifier> STAR = Arrays.asList(
            new Identifier("twp","textures/gui/star_01.png"),
            new Identifier("twp","textures/gui/star_02.png"),
            new Identifier("twp","textures/gui/star_03.png"),
            new Identifier("twp","textures/gui/star_04.png"),
            new Identifier("twp","textures/gui/star_05.png"),
            new Identifier("twp","textures/gui/star_06.png"),
            new Identifier("twp","textures/gui/star_07.png"),
            new Identifier("twp","textures/gui/star_08.png"),
            new Identifier("twp","textures/gui/star_09.png")
    );

    public AnimatedButtonWidget(String name,int x, int y, PressAction onPress) {
        super(x, y, 16, 16, Text.of(name), onPress);
        disabled=true;
        complete=false;
        preconditions=new ArrayList<>();
    }


    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        ticks++;
        if(ticks>=10)
        {
            count++;
            if(count==STAR.size())
            {
                count=0;
            }
            ticks=0;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0,STAR.get(count));

        if(complete)
        {
            RenderSystem.setShaderColor(1,1,0,1);
            this.active=false;
            this.disabled=false;
        }
        else if(arePreconditionsMet())
        {
            this.disabled=false;
            this.active=true;
            if(isHovered())
            {
                RenderSystem.setShaderColor(1,0,0,1);
            }
        }

        if(this.disabled)
        {
            RenderSystem.setShaderColor(0.1f,0.1f,0.1f,0.5f);
            this.active=false;
        }

        RenderSystem.enableDepthTest();
        drawTexture(matrices,x,y,0,0,16,16,16,16);
        RenderSystem.setShaderColor(1,1,1,1);

    }

    public boolean isDisabled() {
        return disabled;
    }

    public void addPrecondition(AnimatedButtonWidget precondition)
    {
        preconditions.add(precondition);
    }

    public List<AnimatedButtonWidget> getPreconditions() {
        return preconditions;
    }

    public boolean arePreconditionsMet()
    {

        for(AnimatedButtonWidget animatedButtonWidget: preconditions)
        {
            if(!animatedButtonWidget.complete)
            {
                return false;
            }
        }

        return true;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void buttonCommand(String command)
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(getMessage().asString());
        buf.writeString(command);
        ClientPlayNetworking.send(TWP.STAT_PACKET,buf);
        complete=true;
    }
}
