package mgl.lab02c2016;


        import android.net.Uri;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.method.ScrollingMovementMethod;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.RadioGroup;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;


        import java.text.DecimalFormat;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;
        import java.util.Random;

public class MainActivity extends AppCompatActivity implements  RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    DecimalFormat f = new DecimalFormat("##.00");

    ElementoMenu[] listaBebidas;
    ElementoMenu[] listaPlatos;
    ElementoMenu[] listaPostre;

    private Spinner spinnerHorario;
    private TextView pedido;
    private RadioGroup radioGroup;
    private Button botonAgregar;
    private Button botonConfirmar;
    private Button botonReiniciar;
    private ListView listaProductos;
    ArrayAdapter<ElementoMenu> adapter;
    ArrayList<ElementoMenu> listaElementos = new ArrayList<>();

    boolean pedidoConfirmado=false;
    Double total;

    ArrayList<ElementoMenu> listaPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pedido = (TextView) findViewById(R.id.textViewPedido);
        pedido.setMovementMethod(new ScrollingMovementMethod());

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        botonAgregar =(Button) findViewById(R.id.buttonAgregar);
        botonAgregar.setOnClickListener(this);
        botonConfirmar = (Button) findViewById(R.id.buttonConfirmar);
        botonConfirmar.setOnClickListener(this);
        botonReiniciar = (Button) findViewById(R.id.buttonReiniciar);
        botonReiniciar.setOnClickListener(this);

        listaProductos =(ListView) findViewById(R.id.listViewProductos);

        llenarSpinner();
        iniciarListas();
        adapter = new ArrayAdapter<ElementoMenu>(
                this, android.R.layout.simple_list_item_single_choice, listaElementos);
        listaProductos.setAdapter(adapter);

        listaPedidos = new ArrayList<ElementoMenu>();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int idRadioCheked = radioGroup.getCheckedRadioButtonId();
        switch(idRadioCheked) {
            case R.id.radioButtonPlato:
                listaElementos.clear();
                listaElementos.addAll(Arrays.asList(listaPlatos));
                adapter.notifyDataSetChanged();
                break;
            case R.id.radioButtonPostre:
                listaElementos.clear();
                listaElementos.addAll(Arrays.asList(listaPostre));
                adapter.notifyDataSetChanged();
                break;
            case R.id.radioButtonBebida:
                listaElementos.clear();
                listaElementos.addAll(Arrays.asList(listaBebidas));
                adapter.notifyDataSetChanged();
                break;
            case -1:
                //esto es error
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonAgregar:
                if(listaProductos.getCheckedItemCount() == 1 && pedidoConfirmado == false){
                    int posicisionSelec = listaProductos.getCheckedItemPosition();
                    listaPedidos.add((ElementoMenu) listaProductos.getItemAtPosition(posicisionSelec));
                    actualizarPedidos();
                    listaProductos.clearChoices();
                    listaProductos.setItemChecked(-1,true);
                }
                else if (pedidoConfirmado)
                    Toast.makeText(this,"El pedido ha sido confirmado",Toast.LENGTH_LONG).show();
                else{
                    Toast.makeText(this,"Debe seleccionar algo del menu",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.buttonConfirmar:
                if(listaPedidos.isEmpty()) {
                    Toast.makeText(this, "Debe agregar un producto al pedido", Toast.LENGTH_LONG).show();
                }else {
                    pedidoConfirmado = true;
                    Toast.makeText(this, "Pedido confirmado", Toast.LENGTH_LONG).show();
                    actualizarPedidos();
                }
                break;
            case R.id.buttonReiniciar:
                listaPedidos.clear();
                actualizarPedidos();
                pedidoConfirmado=false;
                break;
        }
    }



    class ElementoMenu {
        private Integer id;
        private String nombre;
        private Double precio;

        public ElementoMenu() {
        }

        public ElementoMenu(Integer i, String n, Double p) {
            this.setId(i);
            this.setNombre(n);
            this.setPrecio(p);
        }

        public ElementoMenu(Integer i, String n) {
            this(i,n,0.0);
            Random r = new Random();
            this.precio= (r.nextInt(3)+1)*((r.nextDouble()*100));
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Double getPrecio() {
            return precio;
        }

        public void setPrecio(Double precio) {
            this.precio = precio;
        }

        @Override
        public String toString() {
            return this.nombre+ "( "+f.format(this.precio)+")";
        }
    }

    private void actualizarPedidos() {
        if(listaPedidos.isEmpty()){
            pedido.setText("");
        }
        else{
            String strElemento = "";
            total = 0.0;
            for (ElementoMenu elemento: listaPedidos) {
                strElemento += elemento.toString() + "\n";
                total += elemento.getPrecio();
            }
            if(pedidoConfirmado)
                strElemento += "Total: " + total.toString()+"\n";
            pedido.setText(strElemento);
            pedido.setVisibility(View.VISIBLE);
        }

    }

    private void llenarSpinner(){
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("20.00");
        spinnerArray.add("20.30");
        spinnerArray.add("21.00");
        spinnerArray.add("21.30");
        spinnerArray.add("22.00");
        spinnerArray.add("22.30");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorario = (Spinner) findViewById(R.id.spinnerHorario);
        spinnerHorario.setAdapter(adapter);
    }
    private void iniciarListas(){
        // inicia lista de bebidas
        listaBebidas = new ElementoMenu[7];
        listaBebidas[0]=new ElementoMenu(1,"Coca");
        listaBebidas[1]=new ElementoMenu(4,"Jugo");
        listaBebidas[2]=new ElementoMenu(6,"Agua");
        listaBebidas[3]=new ElementoMenu(8,"Soda");
        listaBebidas[4]=new ElementoMenu(9,"Fernet");
        listaBebidas[5]=new ElementoMenu(10,"Vino");
        listaBebidas[6]=new ElementoMenu(11,"Cerveza");
        // inicia lista de platos
        listaPlatos= new ElementoMenu[14];
        listaPlatos[0]=new ElementoMenu(1,"Ravioles");
        listaPlatos[1]=new ElementoMenu(2,"Gnocchi");
        listaPlatos[2]=new ElementoMenu(3,"Tallarines");
        listaPlatos[3]=new ElementoMenu(4,"Lomo");
        listaPlatos[4]=new ElementoMenu(5,"Entrecot");
        listaPlatos[5]=new ElementoMenu(6,"Pollo");
        listaPlatos[6]=new ElementoMenu(7,"Pechuga");
        listaPlatos[7]=new ElementoMenu(8,"Pizza");
        listaPlatos[8]=new ElementoMenu(9,"Empanadas");
        listaPlatos[9]=new ElementoMenu(10,"Milanesas");
        listaPlatos[10]=new ElementoMenu(11,"Picada 1");
        listaPlatos[11]=new ElementoMenu(12,"Picada 2");
        listaPlatos[12]=new ElementoMenu(13,"Hamburguesa");
        listaPlatos[13]=new ElementoMenu(14,"Calamares");
        // inicia lista de postres
        listaPostre= new ElementoMenu[15];
        listaPostre[0]=new ElementoMenu(1,"Helado");
        listaPostre[1]=new ElementoMenu(2,"Ensalada de Frutas");
        listaPostre[2]=new ElementoMenu(3,"Macedonia");
        listaPostre[3]=new ElementoMenu(4,"Brownie");
        listaPostre[4]=new ElementoMenu(5,"Cheescake");
        listaPostre[5]=new ElementoMenu(6,"Tiramisu");
        listaPostre[6]=new ElementoMenu(7,"Mousse");
        listaPostre[7]=new ElementoMenu(8,"Fondue");
        listaPostre[8]=new ElementoMenu(9,"Profiterol");
        listaPostre[9]=new ElementoMenu(10,"Selva Negra");
        listaPostre[10]=new ElementoMenu(11,"Lemon Pie");
        listaPostre[11]=new ElementoMenu(12,"KitKat");
        listaPostre[12]=new ElementoMenu(13,"IceCreamSandwich");
        listaPostre[13]=new ElementoMenu(14,"Frozen Yougurth");
        listaPostre[14]=new ElementoMenu(15,"Queso y Batata");

    }

}