/**
 * Paint.exe, but yk, Java 98' edition.
 * Lab 6, written by Benjamin Stålbert Holm,
 * Karl Ryoma Inoue Olsson & Tim Ahlqvist,
 * */


//Imports
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Stack; // Basically just a fancy list
import javax.imageio.ImageIO;
import javax.swing.*; // Main interface import
import java.io.File;


/**
 * This is the ViewManager, which acts as the users
 * primary way of interacting with the program, through
 * a nice and easy to use Visual Interface. This class
 * handles everything from tracking the cursor, the buttons
 * and so forth. It extends JPanel. It does not handle
 * the actual drawing, DrawEngine does. -->
 *
 * The project is based on a two-system one rule. Meaning
 * that we use two classes, the ViewManager which handles
 * the entire interaction with the user, and then we have
 * the DrawEngine, which handles the actually complex task
 * of handling and rendering the user inputs, so they can
 * create beautiful drawings... or ugly ones, if they prefer.
 *
 */

class ViewManager extends JPanel{

    // Initiliase
    private JFrame frame;
    private DrawEngine drawEngine;

    // Core of the Paint Engine
    public ViewManager(){

        // Intitliase all the (base) visual things
        frame = new JFrame("Paint.exe - Java 98' Edition"); // Name
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit on close
        frame.setSize(800,600); // Size of window (in pixels)
        frame.setResizable(false); // Makes it easier to design the GUI
        frame.add(this); // Add all above
        //All the panels
        frame.setLayout(null); // We want full control

        // Top Panel
        JPanel topPanel = new JPanel(); // Panel (See as "subframe" if you wish)
        topPanel.setLayout(null); // Remove preset layout
        topPanel.setBackground(Color.cyan); // Background colour
        topPanel.setBounds(0,0,800,25); //Where it's placed

        // Save image button
        JButton file = new JButton("Save Image"); // Make a button with the text "Save Image"
        file.setBounds(-5,0,100,25); // Placement and size
        file.addActionListener(e -> drawEngine.saveToFile()); // Actually make it do things (call the DrawEngine)
        topPanel.add(file); // Append

        // Undo button
        JButton undoBtn = new JButton("Undo");
        undoBtn.setBounds(85, 0, 60, 25);
        undoBtn.addActionListener(e -> drawEngine.undo());
        topPanel.add(undoBtn);

        //Shape buttons
        // Pencil
        JButton pencil = new JButton("Pencil");
        pencil.setBounds(730,0,70,25);
        pencil.addActionListener(e -> drawEngine.setMode("pencil"));
        topPanel.add(pencil);

        // Rectangle
        JButton rect = new JButton("Rectangle");
        rect.setBounds(652,0,90,25);
        rect.addActionListener(e -> drawEngine.setMode("rect"));
        topPanel.add(rect);

        // Oval
        JButton oval = new JButton("Oval");
        oval.setBounds(604,0,60,25);
        oval.addActionListener(e -> drawEngine.setMode("oval"));
        topPanel.add(oval);

        // Triangle
        JButton tria = new JButton("Triangle");
        tria.setBounds(541,0,75,25);
        tria.addActionListener(e -> drawEngine.setMode("tria"));
        topPanel.add(tria);

        // Line
        JButton line = new JButton("Line");
        line.setBounds(493,0,60,25);
        line.addActionListener(e -> drawEngine.setMode("line"));
        topPanel.add(line);

        // Toggle fill
        JToggleButton button = new JToggleButton("Fill");
        button.setBounds(445,0,60,25);
        button.addItemListener(e -> drawEngine.fillState(button.isSelected()));
        topPanel.add(button);


        // (Left) Side Panel
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(null);
        sidePanel.setBackground(Color.blue);
        sidePanel.setBounds(0,25,120,525);

        // All the colours... or well, 32 of them atleast.
        Color[] colours = {
                Color.BLACK, Color.WHITE,
                new Color(128, 128, 128),
                new Color(192, 192, 192),
                new Color(128, 0, 0),
                Color.RED,
                new Color(128, 0, 128),
                Color.MAGENTA,
                new Color(0, 128, 0),
                new Color(0, 255, 0),
                new Color(128, 128, 0),
                Color.YELLOW,
                new Color(0, 0, 128),
                Color.BLUE,
                new Color(0, 128, 128),
                Color.CYAN,
                new Color(255, 128, 128),
                new Color(255, 192, 128),
                new Color(255, 255, 128),
                new Color(192, 255, 192),
                new Color(128, 255, 255),
                new Color(192, 192, 255),
                new Color(255, 128, 255),
                new Color(255, 192, 255),
                new Color(64, 0, 0),
                new Color(128, 64, 0),
                new Color(0, 64, 0),
                new Color(0, 64, 64),
                new Color(0, 0, 64),
                new Color(64, 0, 64),
                new Color(64, 64, 64),
                new Color(255, 128, 64)
        };

        //Placing out the colour blocks
        int y = 10;
        for (int i = 0; i < 32; i += 2){
            //Get colours
            Color colourL = colours[i];
            Color colourR =colours[i+1];

            //Fix buttons so they get a nice colours
            JButton CBL = new JButton();
            CBL.setBackground(colourL);
            JButton CBR = new JButton();
            CBR.setBackground(colourR);
            CBR.setOpaque(true);
            CBR.setBorderPainted(false);
            CBL.setBackground(colourL);
            CBL.setOpaque(true);
            CBL.setBorderPainted(false);

            //Place them out and add action listender going to drawengine
            CBL.setBounds(10,y,30,30);
            CBR.setBounds(40, y,30,30);
            CBL.addActionListener(e -> drawEngine.setCurrentColor(colourL));
            CBR.addActionListener(e -> drawEngine.setCurrentColor(colourR));
            sidePanel.add(CBL);
            sidePanel.add(CBR);
            y += 30;

        }

        //Adding a slider (for the width)
        JSlider strokeSlider = new JSlider(1,30,5);
        strokeSlider.setBounds(5,490,110,40);
        strokeSlider.addChangeListener(e -> drawEngine.setStrokeWidth(strokeSlider.getValue()));
        sidePanel.add(strokeSlider);

        // Credits panel (bottom)
        JPanel credits = new JPanel();
        credits.setBackground(Color.BLACK);
        credits.setBounds(0,550, 800, 25);
        credits.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel creditText = new JLabel("Proudly made by Group 39 ©");
        creditText.setForeground(Color.cyan);
        credits.add(creditText);


        // Canvas panel (place for fun drawing time :D)
        /**
         * Here we place down the DrawEngine, which really to the
         * user just appears as a new panel (which it technically is.)
         * We're easily able to set the bounds of it, which I hope
         * should make it easier to later implment a button to rotate
         * it 90 degrees. I think... atleast, i havent really thought
         * it through to thourhgly.
         */
        drawEngine = new DrawEngine();
        drawEngine.setBounds(145,50,630,475);



        // Render (i.e add all the panels)
        frame.add(topPanel);
        frame.add(sidePanel);
        frame.add(drawEngine);
        frame.add(credits);





        // Place it on the screen
        frame.setLocationRelativeTo(null); //null being center
        frame.setVisible(true); // Make it visible

    }


}

/**
 *  This is the DrawEngine, which is where the actual
 *  "magic" happens, that meaning it handles all the
 *  brushstrokes, rectangles, ovals, and other fun
 *  features if we cared enough to add them (no promises...)
 */
class DrawEngine extends JPanel implements MouseListener, MouseMotionListener {
    // Intiliase the base for the class
    private Image canvas;
    private Graphics2D graphics;

    private int startX, startY, currentX, currentY;
    private String mode = "pencil"; // Intital mode state
    private boolean bStat = false;
    private Color currentColor = Color.blue; // Initital colour
    private int strokeWidth = 5; // Initial width (affects all modes)

    //preview for shapes thingies
    private int dragX, dragY;
    private boolean dragging = false;

    // What can be accesed by the program
    public DrawEngine(){
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // These make it really easy to change the settings
    public void setMode(String m){mode = m;} //Change mode
    public void fillState(Boolean s){bStat = s;}
    public void setCurrentColor(Color c){currentColor = c;} //Change colour
    public void setStrokeWidth(int w){strokeWidth = w;} // Change width

    //makeTriangle helper function
    private Polygon makeTriangle(int x1,int y1,int x2,int y2){

        // Math, still a bit wonky.
        int midX = (x1+x2)/2;
        int halfWidth = Math.abs(x2 - x1)/2;
        int leftX = midX - halfWidth;
        int rightX = midX + halfWidth;
        int[] xPoints = {x1, rightX,leftX };
        int[] yPoints = {y1, y2, y2};
        Polygon triangle = new Polygon(xPoints, yPoints, 3);
        return triangle; // Triangle :D
    }

    //Undo feature requires to save the previous states of the drawing
    private Stack<Image> undoStack = new Stack<>(); //Fancy list more or less
    private void saveState(){
        Image snapshot = createImage(getWidth(),getHeight()); // Make image
        Graphics g = snapshot.getGraphics(); //g is the graphics of the snapshot
        g.drawImage(canvas,0,0,null); //we draw the image (canvas) at (0,0)
        g.dispose(); // We dispose.
        undoStack.push(snapshot); //We add it to the stack
    }
    public void undo() {
        if (!undoStack.isEmpty()){ //If its NOT empty
            canvas = undoStack.pop(); //remove the last elemnent
            graphics = (Graphics2D) canvas.getGraphics(); //paint the second latest (now latest) snapshot on the canvas
            repaint();
        }
    }

    public void saveToFile(){
        JFileChooser chooser = new JFileChooser(); //Create file chooser
        chooser.setDialogTitle("Save your masterpiece :D"); //title
        int choice = chooser.showSaveDialog(this); //get the choice
        if (choice != JFileChooser.APPROVE_OPTION) return; //if not approved ;(

        try {
            BufferedImage bImage = new BufferedImage(canvas.getWidth(null),
                    canvas.getHeight(null), BufferedImage.TYPE_INT_RGB); //Buffered image
            Graphics2D gImage = bImage.createGraphics(); // Graphics image
            gImage.drawImage(canvas,0,0,null); // Get the canvas (drawing) on saing image
            gImage.dispose(); //Get rid of it now that it doesn't serve us anymore.

            File file = chooser.getSelectedFile(); // New file
            if (!file.getName().toLowerCase().endsWith(".png")) file = new File(file.getAbsoluteFile()+".png"); //Ensure it get saved as png
            ImageIO.write(bImage, "png", file); //Actually save the image
        } catch (Exception e) {
            throw new RuntimeException("Dang, we got the error: " + e); //Something went wrong (probably)
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //If the canvas is null, we fill it with white.
        if (canvas == null){
            canvas = createImage(getWidth(),getHeight()); // Fit canvas
            graphics = (Graphics2D) canvas.getGraphics();
            graphics.setColor(Color.white);
            graphics.fillRect(0,0,getWidth(),getHeight());
        }
        g.drawImage(canvas,0,0,null);

        //Preview
        if (dragging && !mode.equals("pencil")){
            Graphics2D previewGraphics = (Graphics2D) g.create(); // Create new graphics "layer"
            previewGraphics.setColor(currentColor);
            previewGraphics.setStroke(new BasicStroke(strokeWidth));
            // math
            int x = Math.min(startX, dragX);
            int y = Math.min(startY, dragY);
            int w = Math.abs(dragX - startX);
            int h = Math.abs(dragY - startY);
            // draw the preview
            if (mode.equals("rect")) {if(bStat){previewGraphics.fillRect(x,y,w,h);}else{previewGraphics.drawRect(x,y,w,h);}}
            if (mode.equals("oval")) {if(bStat){previewGraphics.fillOval(x,y,w,h);}else{previewGraphics.drawOval(x,y,w,h);}}
            if (mode.equals("tria")) {if(bStat){previewGraphics.fillPolygon(makeTriangle(dragX,dragY,startX,startY));}else{
                previewGraphics.drawPolygon(makeTriangle(dragX,dragY,startX,startY));
            }}
            if (mode.equals("line")) {
                previewGraphics.setStroke(new BasicStroke(strokeWidth));
                previewGraphics.drawLine(startX,startY,dragX,dragY);
            }
            previewGraphics.dispose(); // get rid of it.
        }

    }



    @Override
    public void mousePressed(MouseEvent e) {

        // Get cords (Short for cordinates btw)
        startX = e.getX();
        startY = e.getY();

        saveState(); // needed for undo feature

        // Make ---------- (image thats a brushstroke)
        if (mode.equals("pencil")){
            graphics.setColor(currentColor);
            graphics.setStroke(new BasicStroke(strokeWidth));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mode.equals("pencil")){
            graphics.setColor(currentColor);
            graphics.setStroke(new BasicStroke(strokeWidth));
            graphics.drawLine(startX,startY,e.getX(),e.getY()); //Creates a continous line
            // ^^ Does get blocky when drawing, but preferbly that compared to gaps that we get if we use oval.
            startX = e.getX();
            startY = e.getY();
            repaint();
        } else {
            // These are used for the other modes
            dragX = e.getX();
            dragY = e.getY();
            dragging = true; // This is for preview
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        // Get cords
        currentX = e.getX();
        currentY = e.getY();
        // Colour and width
        graphics.setColor(currentColor);
        graphics.setStroke(new BasicStroke(strokeWidth));
        // Check which mode, then do thing based on that mode.
        // Bunch of alghoritms to make it as easy to use as possible for the user.
        if (mode.equals("rect")) {
            if (bStat){graphics.fillRect(Math.min(startX, currentX), Math.min(startY, currentY), Math.abs(currentX - startX), Math.abs(currentY - startY));}
            else {graphics.drawRect(Math.min(startX, currentX), Math.min(startY, currentY), Math.abs(currentX - startX), Math.abs(currentY - startY));}
        } else if (mode.equals("oval")) {
            if (bStat){graphics.fillOval(Math.min(startX, currentX), Math.min(startY, currentY), Math.abs(currentX - startX), Math.abs(currentY - startY));}
            else {graphics.drawOval(Math.min(startX, currentX), Math.min(startY, currentY), Math.abs(currentX - startX), Math.abs(currentY - startY));}
        } else if (mode.equals("tria")) {
            if (bStat){graphics.fillPolygon(makeTriangle(currentX,currentY,startX,startY));} // Uses helper function makeTriangle
            else {graphics.drawPolygon(makeTriangle(currentX,currentY,startX,startY));}
        } else if (mode.equals("line")) {
            graphics.drawLine(startX,startY,currentX,currentY);
        }
        dragging = false;
        dragX = dragY = 0; // avoid bug
        repaint();
    }

    // Make a dot.
    public void mouseClicked(MouseEvent e) {
        // Get cords
        currentX = e.getX();
        currentY = e.getY();
        // Set colour
        graphics.setColor(currentColor);
        //Make a (filled) rectangle, i.e the dot.
        graphics.fillRect(currentX,currentY, strokeWidth, strokeWidth);
        repaint(); // show

    }

    // None of these are used, but the program gets mad if they arent here
    // Due to the "implements" part.
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}

// Class Draw and main, doesnt od anything beyond calling ViewManager
class Draw {
    public static void main(String[] args){
        // Read that invokeLater would make the program more stable.
        SwingUtilities.invokeLater(() -> {
            new ViewManager();
        });
    }
}
