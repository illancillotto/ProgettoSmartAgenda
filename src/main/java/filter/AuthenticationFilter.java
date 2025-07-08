package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Utente;

public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializzazione del filtro
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Ottieni la sessione
        HttpSession session = httpRequest.getSession(false);

        // Verifica se l'utente è autenticato
        boolean isAuthenticated = (session != null && session.getAttribute("utente") != null);

        // Ottieni l'URI richiesto
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Se l'utente non è autenticato, reindirizza al login
        if (!isAuthenticated) {
            httpResponse.sendRedirect(contextPath + "/jsp/login.jsp");
            return;
        }

        // Verifica autorizzazioni per pagine admin
        if (requestURI.contains("/admin") || requestURI.contains("admin.jsp")) {
            Utente utente = (Utente) session.getAttribute("utente");
            if (utente == null || !utente.getRuolo().equals("admin")) {
                httpResponse.sendRedirect(contextPath + "/jsp/home.jsp?errore=accesso_negato");
                return;
            }
        }

        // Continua con la catena di filtri
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup del filtro
    }
}