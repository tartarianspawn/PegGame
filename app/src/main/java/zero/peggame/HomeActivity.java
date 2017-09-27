package zero.peggame;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    ImageView[][] peg = new ImageView[5][5];
    Boolean[][] activePeg;
    ImageView startPeg, endPeg;
    GridLayout boardLayout;
    GridLayout.LayoutParams params1, params2, params3;
    String DEBUG_TAG = "MY_DEBUG_TAG";
    String ERROR_TAG = "MY_ERROR_TAG";
    ArrayList<View> pegTargets = new ArrayList<View>();
    Button resetButton;
    TextView winText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
        setContentView(R.layout.board2);

        /*
        * The pegs are stored in an array to represent how they look in the grid layout
        * */
        peg[0][0] = (ImageView)findViewById(R.id.pegView1);
        peg[1][0] = (ImageView)findViewById(R.id.pegView2);
        peg[1][1] = (ImageView)findViewById(R.id.pegView3);
        peg[2][0] = (ImageView)findViewById(R.id.pegView4);
        peg[2][1] = (ImageView)findViewById(R.id.pegView5);
        peg[2][2] = (ImageView)findViewById(R.id.pegView6);
        peg[3][0] = (ImageView)findViewById(R.id.pegView7);
        peg[3][1] = (ImageView)findViewById(R.id.pegView8);
        peg[3][2] = (ImageView)findViewById(R.id.pegView9);
        peg[3][3] = (ImageView)findViewById(R.id.pegView10);
        peg[4][0] = (ImageView)findViewById(R.id.pegView11);
        peg[4][1] = (ImageView)findViewById(R.id.pegView12);
        peg[4][2] = (ImageView)findViewById(R.id.pegView13);
        peg[4][3] = (ImageView)findViewById(R.id.pegView14);
        peg[4][4] = (ImageView)findViewById(R.id.pegView15);

        // This half of the array is empty
        // if column > row  object = null
        peg[0][1] = null;
        peg[0][2] = null;
        peg[0][3] = null;
        peg[0][4] = null;
        peg[1][2] = null;
        peg[1][3] = null;
        peg[1][4] = null;
        peg[2][3] = null;
        peg[2][4] = null;
        peg[3][4] = null;

        //Adding the touch listeners
        //boardLayout = (GridLayout)findViewById(R.id.boardLayout);

        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData.Item item =  new ClipData.Item((CharSequence)view.getTag());
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                ClipData dragData = new ClipData(view.getTag().toString(), mimeTypes, item);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                view.startDrag(dragData, shadowBuilder, null, 0);
                return true;
            }
        };

        View.OnDragListener dragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch(dragEvent.getAction()){
                    case DragEvent.ACTION_DRAG_STARTED:
//                        params1 = (GridLayout.LayoutParams)view.getLayoutParams();
//                        Log.d(DEBUG_TAG, "Action is DragEvent.ACTION_DRAG_STARTED");
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
//                        Log.d(DEBUG_TAG, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int)dragEvent.getX();
                        int y_cord = (int)dragEvent.getY();
                        View endPoint;
                        View startPoint = view;
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION:
//                        Log.d(DEBUG_TAG, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        x_cord = (int)dragEvent.getX();
                        y_cord = (int)dragEvent.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
//                        Log.d(DEBUG_TAG, "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int)dragEvent.getX();
                        y_cord = (int)dragEvent.getY();
                        break;

                    case DragEvent.ACTION_DROP:
//                        Log.d(DEBUG_TAG, "Action is DragEvent.ACTION_DROP");
                        /*x_cord = (int)dragEvent.getX();
                        y_cord = (int)dragEvent.getY();
                        x_cord += view.getX() + view.getWidth()/2;
                        y_cord += view.getY() + view.getHeight()/2;
                        endPoint = getTarget(x_cord, y_cord);*/
                        if( view != null){
                            // The label of the clip description is the tag in this case
                            jumpPeg(getPeg(dragEvent.getClipDescription().getLabel().toString()), view);
                            if(endOfGame()){
                                winText.setVisibility(View.VISIBLE);
                            }

                        }
                        // Do actions here
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
//                        Log.d(DEBUG_TAG, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        break;

                    default: break;
                }
                return true;
            }
        };

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    ClipData dragData = ClipData.newPlainText(view.getTag().toString(), "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                    view.startDrag(dragData, shadowBuilder, view, 0);

                    return true;
                }
                else{
                    return false;
                }
            }
        };



        //Set the listeners but only for the accesible pegs
        pegTargets.clear();
        for(int i = 0; i < 5; i++){
            for(int j = 0; j <= i; j++){
                peg[i][j].setOnLongClickListener(longClickListener);
                //peg[i][j].setOnTouchListener(touchListener);
                peg[i][j].setOnDragListener(dragListener);
                pegTargets.add(peg[i][j]);
            }
        }

        resetButton = (Button)findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        winText = (TextView)findViewById(R.id.winTextView);

        startGame();
    }

    public void startGame(){
        // Start a random seeded number for assigning colors
        Random randColor = new Random(System.currentTimeMillis());

        activePeg = new Boolean[5][5];
        // The top peg is always empty
        activePeg[0][0] = false;
        peg[0][0].setImageResource(R.drawable.no_peg);
        for(int i = 1; i < 5;i++){
            for(int j = 0; j <=i; j++){
                // Sets the peg to a random color
                switch(randColor.nextInt(4)){
                    case 0:
                        peg[i][j].setImageResource(R.drawable.blue_peg);
                        break;

                    case 1:
                        peg[i][j].setImageResource(R.drawable.red_peg);
                        break;

                    case 2:
                        peg[i][j].setImageResource(R.drawable.white_peg);
                        break;

                    case 3:
                        peg[i][j].setImageResource(R.drawable.yellow_peg);
                        break;

                    default:
                        peg[i][j].setImageResource(R.drawable.white_peg);
                        break;
                }
                activePeg[i][j] = true;
            }
        }
        winText.setVisibility(View.GONE);
    }

    /**
     * Checks for any available moves. If any are found it returns immediately.
     * @return true is game has ended, false otherwise
     */
    public boolean endOfGame(){
        int availableMoves = 0, x, y;
        int a = -1, b = 1;
        for(int i = 0; i < 5;i++){
            for(int j = 0; j <=i; j++){
                 // don't check if there is no peg
                if(!activePeg[i][j]){
                    continue;
                }

                // Save the spot where you started
                x = i; y = j;
                // Check that there is a peg next to it in any direction
                for(x -= 1; x <= i + 1; x++){
                    for(y -= 1; y <= j + 1; y++){
                        //Skip if its the center of the check
                        if((x == i) && (y == j)){
                            continue;
                        }
                        //Skip out of bounds
                        if((x < 0) || (x > 4) || (y < 0) || (y > 4)){
                            continue;
                        }
                        // If it has a peg adjacent, check if you can hop it, meaning there is a move available and you can return
                        // make sure it is not one of the unreachable areas as well
                        if((activePeg[x][y] != null) && activePeg[x][y]){
                            a = (2*x) - i;
                            b = (2*y) - j;
                            //Check for out of bounds again
                            if((a < 0) || (a > 4) || (b < 0) || (b > 4)){
                                continue;
                            }
                            //Check if there is an empty spot to jump to
                            // if there is the the game has not ended
                            if((activePeg[a][b] != null) && !activePeg[a][b]){
                                return false;
                            }

                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Gets a peg from a tag
     * @param name
     * @return
     */
    public ImageView getPeg(String name){
        int location = Integer.parseInt(name.substring(3)) - 1;

        return (ImageView)pegTargets.get(location);
    }

    public int[] getOrder(String name){
        int location = Integer.parseInt(name.substring(3));
        int[] index = new int[2];

        switch(location){
            case 1: index[0] = 0; index[1] = 0;
                break;
            case 2: index[0] = 1; index[1] = 0;
                break;
            case 3: index[0] = 1; index[1] = 1;
                break;
            case 4: index[0] = 2; index[1] = 0;
                break;
            case 5: index[0] = 2; index[1] = 1;
                break;
            case 6: index[0] = 2; index[1] = 2;
                break;
            case 7: index[0] = 3; index[1] = 0;
                break;
            case 8: index[0] = 3; index[1] = 1;
                break;
            case 9: index[0] = 3; index[1] = 2;
                break;
            case 10: index[0] = 3; index[1] = 3;
                break;
            case 11: index[0] = 4; index[1] = 0;
                break;
            case 12: index[0] = 4; index[1] = 1;
                break;
            case 13: index[0] = 4; index[1] = 2;
                break;
            case 14: index[0] = 4; index[1] = 3;
                break;
            case 15: index[0] = 4; index[1] = 4;
                break;
            default: index[0] = -1; index[1] = -1;
                break;
        }
        return index;
    }

    public boolean jumpPeg(View start, View end){
        int[] peg1, peg2, peg3;
        Drawable color_peg;

        peg1 = getOrder(start.getTag().toString());
        peg3 = getOrder(end.getTag().toString());

        if((activePeg[peg1[0]][peg1[1]] == null) || !activePeg[peg1[0]][peg1[1]]){
            // Either it is not a valid peg or is an empty spot
            // Start spot must have a valid peg
            return false;
        }

        if((activePeg[peg3[0]][peg3[1]] == null) || activePeg[peg3[0]][peg3[1]]){
            // Either it is not a valid peg or is not an empty spot
            // End spot must be empty
            return false;
        }

        // Get index of the peg to remove
        peg2 = new int[2];
        peg2[0] = (peg3[0] + peg1[0])/2;
        peg2[1] = (peg3[1] + peg1[1])/2;

        int[] distance1 = new int[2], distance2 = new int[2];
        distance1[0] = peg2[0] - peg1[0];
        distance1[1] = peg2[1] - peg1[1];
        distance2[0] = peg2[0] - peg3[0];
        distance2[1] = peg2[1] - peg3[1];

        // Make sure peg was actually moved
        // this happens when trying to move exactly one peg over
        if(((distance1[0] == 0) && (distance1[1] == 0)) || ((distance2[0] == 0) && (distance2[1] == 0))){
            return  false;
        }

        // Make sure the distance is at most one in all directions
        if((Math.abs(distance1[0]) <= 1) && (Math.abs(distance1[1]) <= 1) &&
            (Math.abs(distance2[0]) <= 1) && (Math.abs(distance2[1]) <= 1)){
            if((Math.abs(distance1[0]) >= -1) && (Math.abs(distance1[1]) >= -1) &&
                    (Math.abs(distance2[0]) >= -1) && (Math.abs(distance2[1]) >=- 1)){

                //If it has reached here then it is an adjacent peg to both so it is a legal move
                // We make the peg transparent and "remove it"
                peg[peg2[0]][peg2[1]].setImageResource(R.drawable.no_peg);
                activePeg[peg2[0]][peg2[1]] = false;

                //Set the previous peg to empty and not an active spot
                color_peg = peg[peg1[0]][peg1[1]].getDrawable();
                activePeg[peg1[0]][peg1[1]] = false;
                peg[peg1[0]][peg1[1]].setImageResource(R.drawable.no_peg);

                //Set the end spot to full and an empty spot
                activePeg[peg3[0]][peg3[1]] = true;
                peg[peg3[0]][peg3[1]].setImageDrawable(color_peg);

                // legal move
                return true;
            }

        }

        // Not a legal move
        return false;
    }

    //TODO: Check if available spot to place peg
    public View getTarget(int x, int y){
        for(View v: pegTargets){
            if((x > v.getLeft()) && (x < v.getRight()) && (y > v.getTop()) && (y < v.getBottom())){
                return v;
            }
            //(x > v.getX()) && (x < (v.getX()+v.getWidth())) && (y > v.getY()) && (y < (v.getY()+v.getHeight()))
        }
        return null;
    }
}
