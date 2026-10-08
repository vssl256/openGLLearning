import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

public class DebugUI {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final Window win;
    private final Character chara;

    public int currentFPS = 0;
    public double deltaTime = 0;
    public float speed = 0;
    public long maxMemory = 0;
    public long usedMemory = 0;
    public String posY = " ";
    public String yVelocity = " ";
    public String targetVelocity = " ";

    private float[] newSpeed;
    private float[] newAccelStrength;

    public DebugUI( Window win, Character chara ) {
        this.win = win;
        this.chara = chara;

        init();
    }

    public void run() {
        imGuiGlfw.newFrame();
        imGuiGl3.newFrame();
        ImGui.newFrame();

        ImGui.begin( "Debug window" );
        draw();
        ImGui.end();

        ImGui.render();
        imGuiGl3.renderDrawData( ImGui.getDrawData() );
    }

    private boolean showText = false;
    private void draw() {
        if ( ImGui.button( "cool button" ) ) {
            showText = true;
        }
        ImGui.sliderFloat( "speed", newSpeed, 0.0f, 100.0f );
        ImGui.sliderFloat( "lerpSpeed", newAccelStrength, 0.1f, 20.0f );
        //chara.speed = newSpeed[ 0 ];
        //chara.accelStrength = newAccelStrength[ 0 ];

        ImGui.text( "Used memory: " + usedMemory + " / " + maxMemory );
        ImGui.text( currentFPS + " FPS" );
        ImGui.text( "Y= " + posY );
        ImGui.text( "Y Velocity = " + yVelocity + "\ntargetVelocity = " + targetVelocity );
        if ( showText ) {
            ImGui.text( deltaTime + " DT" );
            ImGui.text( speed + " speeds/sec" );
            ImGui.sameLine();
            if ( ImGui.button( "hide text" ) ) {
                showText = false;
            }
        }
    }

    private void init() {
        newSpeed = new float[1];
        newSpeed[0] = Engine.MOVEMENT_SPEED;
        newAccelStrength = new float[1];
        newAccelStrength[0] = Transformation.jumpHeight;
        ImGui.createContext();
        imGuiGlfw.init( win.id, true );
        imGuiGl3.init( "#version 330" );
    }

}
