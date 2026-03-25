import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

public class DebugUI {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final Window win;

    public int currentFPS = 0;
    public double deltaTime = 0;

    public DebugUI( Window win ) {
        this.win = win;

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

        if ( !showText ) {
            ImGui.text( currentFPS + " FPS" );
            ImGui.text( deltaTime + " DT" );
            ImGui.sameLine();
            if ( ImGui.button( "hide text" ) ) {
                showText = false;
            }
        }
    }

    private void init() {
        ImGui.createContext();
        imGuiGlfw.init( win.id, true );
        imGuiGl3.init( "#version 330" );
    }

}
