package ace.actually.twp.screens;

import ace.actually.twp.ScreenHelper;
import ace.actually.twp.buttons.AnimatedButtonWidget;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatScreen extends Screen {

    private int quarter;
    private String[] comp;
    List<AnimatedButtonWidget> widgetList;


    private static final Identifier apple = new Identifier("minecraft","textures/item/bamboo.png");
    public StatScreen() {
        super(new LiteralText("Test"));
        comp=new String[]{};
    }
    public StatScreen(String[] completed) {
        super(new LiteralText("Test"));
        comp=completed;
    }

    @Override
    protected void init() {
        quarter = width/2 - width/4;
        super.init();
        widgetList = new ArrayList<>();

        /**int c = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                widgetList.add(this.addDrawableChild(new AnimatedButtonWidget((c+"d"),quarter+(32*i),16+(32*j),a-> ((AnimatedButtonWidget)a).buttonCommand(""))));
                c++;
            }

        }
        widgetList.get(1).addPrecondition(widgetList.get(0));
        widgetList.get(10).addPrecondition(widgetList.get(8));**/

        createChartUseConfig();
        widgetList.forEach(a->
        {
            for (String s : comp) {
                if (a.getMessage().asString().equals(s)) {
                    a.setComplete(true);
                    break;
                }
            }
        });
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {


        fill(matrices,0,0,this.width,this.height, ScreenHelper.RGBA(0,0,0,255));

        //render the lines connecting the stars behind them
        widgetList.forEach(a->
        {
            a.getPreconditions().forEach(b->
            {
                ScreenHelper.line(matrices,a.x+9,a.y+9,b.x+9,b.y+9);
            });
        });

        //render the stars, not done here because they are widgets
        super.render(matrices, mouseX, mouseY, delta);


       //render the stars name over the star
       widgetList.forEach(a->
       {
           if (a.isHovered()) {
               textRenderer.draw(matrices,a.getMessage(),mouseX+5,mouseY+5, ScreenHelper.RGBA(255,255,255,255));
           }

       });



    }

    @Override
    public boolean shouldPause() {
        return false;
    }


    //star name,starx,stary,precondition1;precondition2,commandOnCompletion
    public void createChartUseConfig()
    {
        try {
            List<String> lines = FileUtils.readLines(FabricLoader.getInstance().getConfigDir().resolve("twp/chart.txt").toFile(), Charset.defaultCharset());
            for(String line: lines)
            {
                if(line.startsWith("#"))
                {
                    continue;
                }
                String[] data = line.split(",");
                AnimatedButtonWidget current = this.addDrawableChild(new AnimatedButtonWidget(data[0],Integer.parseInt(data[1]),Integer.parseInt(data[2]),a-> ((AnimatedButtonWidget)a).buttonCommand(data[4])));

                if(data[3].length()==0)
                {
                    widgetList.add(current);
                    continue;
                }
                String[] precons = data[3].split(";");

                for(String precon: precons)
                {
                    for(ButtonWidget widget: widgetList)
                    {
                        if(widget instanceof AnimatedButtonWidget abw)
                        {
                            if(abw.getMessage().asString().equals(precon))
                            {
                                current.addPrecondition(abw);
                            }
                        }
                    }
                }
                widgetList.add(current);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

