public class LifeCells {
    private int width;
    private int height;
    private boolean[][] nowLiving;
    private boolean[][] nextGen;

    public LifeCells(int width, int height) {
        this.width = width;
        this.height = height;
        //現在のセルの生死
        nowLiving = new boolean[this.height][this.width];
        //次の世代のセルの生死
        nextGen = new boolean[this.height][this.width];
    }

    //指定したセルの生死を返す
    public boolean isLiving(int h, int w){
        return nowLiving[h][w];
    }

    //指定したセルの生死を反転させる
    public void ChangeLivingOrDead(int h, int w){
        nowLiving[h][w] = !nowLiving[h][w];
    }
    
    //全てのセルを引数の確率で生きているセルにする
    public void randomSet(double rate) {
        for(int h = 0; h < height; h++){
            for(int w = 0; w < width; w++){
                nowLiving[h][w] = Math.random() < rate;
            }
        }
    }

    //全てのセルを死んでいるセルにする
    public void Clear() {
        for(int h = 0; h < height; h++){
            for(int w = 0; w < width; w++){
                nowLiving[h][w] = false;
            }
        }
    }
    
    //次の世代のセルの生死を判定し、更新する。
    public void proceedGen() {
        int count, g, h, i, v, w, x;
        for (h = 0; h < height; h++) {
            for(w = 0; w < width; w++){
                count = 0;
                //セル全体の上下端・左右端を繋がっているとみなす。
                if(h == 0){
                    g = height - 1;
                    i = h + 1;
                } else if(h == height - 1) {
                    g = h - 1;
                    i = 0;
                } else {
                    g = h - 1;
                    i = h + 1;
                }
                if(w == 0){
                    v = width - 1;
                    x = w + 1;
                } else if(w == width - 1){
                    v = w - 1;
                    x = 0;
                } else {
                    v = w - 1;
                    x = w + 1;
                }

                if(nowLiving[g][v])
                    count++;
                if(nowLiving[g][w])
                    count++;
                if(nowLiving[g][x])
                    count++;
                if(nowLiving[h][v])
                    count++;
                if(nowLiving[h][x])
                    count++;
                if(nowLiving[i][v])
                    count++;
                if(nowLiving[i][w])
                    count++;
                if(nowLiving[i][x])
                    count++;
                
                //誕生3・過疎死1・過密死4（生存2,3）
                if(!nowLiving[h][w] && count == 3) {
                    nextGen[h][w] = true;
                } else if (nowLiving[h][w] && (count <= 1 || count >= 4)) {
                    nextGen[h][w] = false;
                } else {
                    nextGen[h][w] = nowLiving[h][w];
                }
            }
        }

        //セルの更新
        for(h = 0; h < height; h++){
            for(w = 0; w < width; w++){
                nowLiving[h][w] = nextGen[h][w];
            }
        }
    }
    
}
