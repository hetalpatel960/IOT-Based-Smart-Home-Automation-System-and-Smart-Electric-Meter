import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


    
public class finalbill extends HttpServlet {
    private String units;
    private float u;
    private float bill;
    
    private PreparedStatement stmt;
    private int i;
    private Object out;
    private String rel;
    private String tata;
    private String s;
    private String t;
    private String x;
  
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            System.out.println("asdfghjkl;");
            int count;
            count= Integer.parseInt(request.getAttribute("count").toString());
           int sum = Integer.parseInt(request.getAttribute("sum").toString());
       String x= (String) request.getParameter("x");
     
        
           
           
            if(x.equals("1"))
            {
            u = (float) 3.61 ; 
            bill = (float) (((sum/100000)*(count/360)*u) + 50) ;
            }
            else if(x.equals("2"))
            {
            u =  (float) 3.67 ;
            bill = (float) (((sum/100000)*(count/360)*u) + 55) ;
             }
              else 
              {
                System.out.println("Please Select Appropriate Provider");
               }
    
       
               s = request.getParameter("s");
            t = request.getParameter("t");
           
          System.out.println("insert into amt values("+s+","+t+","+bill+")");
                 Class.forName("com.mysql.jdbc.Driver");  
           Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/abcd","root",""); 
        
    
            
            stmt = con.prepareStatement("insert into amt values(?,?,?)");
            
            stmt.setString(1, s);
            stmt.setString(2, t);
            stmt.setFloat(3, bill);
            i= stmt.executeUpdate();
           System.out.println("insert into amt values("+s+","+t+","+bill+")");
    ResultSet rs =stmt.executeQuery("select * from amt");
            
             if (rs!= null)
             {
             out.println("<table border=1 width=50% height=50%>");
             out.println("Your Bill from :");
             out.println("<tr><th>Start Date</th><th>End Date</th><th>Total Bill</th>");
             while (rs.next()) {
                 s = rs.getString("startdate");
                 t = rs.getString("enddate");
                 bill = rs.getFloat("totalbill");
                 
                  out.println("<tr><td>" + s + "</td><td>" + t + "</td><td>" + bill + "</td>"); 
             }
             out.println("</table>");
             out.println("</html></body>");
            
               con.close();
              
             }
        }
                
        catch (ClassNotFoundException ex) {
            Logger.getLogger(finalbill.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(finalbill.class.getName()).log(Level.SEVERE, null, ex);
        } finally {            
            out.close();
        }
    }
}
