package com.example.ecom_project;

import com.example.ecom_project.model.Product;
import com.example.ecom_project.repository.ProductRepo; // adjust import to your actual repo package
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepo repo;

    public DataSeeder(ProductRepo repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repo.count() > 0) return; // don't reseed if data already exists

        record Seed(String name, String desc, String brand, String price,
                    String category, String date, boolean available, int qty, Color color) {}

        List<Seed> seeds = List.of(
                new Seed("Wireless Mouse", "Ergonomic wireless mouse with USB receiver", "Logitech", "799.00", "Electronics", "15-01-2026", true, 120, new Color(66, 133, 244)),
                new Seed("Mechanical Keyboard", "RGB backlit mechanical keyboard, blue switches", "Redragon", "2499.00", "Electronics", "22-02-2026", true, 45, new Color(219, 68, 55)),
                new Seed("Running Shoes", "Lightweight running shoes with cushioned sole", "Nike", "3499.00", "Footwear", "10-03-2026", true, 60, new Color(15, 157, 88)),
                new Seed("Formal Shirt", "Slim fit cotton formal shirt", "Allen Solly", "1299.00", "Apparel", "05-04-2026", true, 200, new Color(244, 180, 0)),
                new Seed("Bluetooth Speaker", "Portable speaker with 12hr battery life", "JBL", "1999.00", "Electronics", "18-05-2026", false, 0, new Color(171, 71, 188)),
                new Seed("Yoga Mat", "Non-slip 6mm thick yoga mat", "Boldfit", "699.00", "Fitness", "01-06-2026", true, 150, new Color(0, 172, 193)),
                new Seed("Backpack", "Water-resistant 30L laptop backpack", "Wildcraft", "1599.00", "Accessories", "12-06-2026", true, 80, new Color(255, 112, 67)),
                new Seed("Coffee Maker", "Automatic drip coffee maker, 1.2L", "Philips", "3299.00", "Home Appliances", "20-07-2026", true, 25, new Color(109, 76, 65)),
                new Seed("Smartwatch", "Fitness smartwatch with heart rate monitor", "Noise", "2799.00", "Electronics", "01-08-2026", true, 95, new Color(94, 53, 177)),
                new Seed("Desk Lamp", "LED desk lamp with adjustable brightness", "Philips", "899.00", "Home", "08-08-2026", true, 110, new Color(67, 160, 71))
        );

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        for (Seed s : seeds) {
            Product p = new Product();
            p.setName(s.name());
            p.setDescription(s.desc());
            p.setBrand(s.brand());
            p.setPrice(new BigDecimal(s.price()));
            p.setCategory(s.category());
            p.setReleaseDate(sdf.parse(s.date()));
            p.setProductAvailable(s.available());
            p.setQuantity(s.qty());
            p.setImageName(s.name().toLowerCase().replace(" ", "_") + ".png");
            p.setImageType("image/png");
            p.setImageData(generatePlaceholderImage(s.name(), s.color()));
            repo.save(p);
        }

        System.out.println("Seeded " + seeds.size() + " products with placeholder images.");
    }

    private byte[] generatePlaceholderImage(String text, Color bgColor) throws IOException {
        int width = 400, height = 400;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(bgColor);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 28));
        FontMetrics fm = g.getFontMetrics();

        // wrap text across two lines if needed
        String[] words = text.split(" ");
        String line1 = words.length > 1 ? words[0] : text;
        String line2 = words.length > 1 ? String.join(" ", java.util.Arrays.copyOfRange(words, 1, words.length)) : "";

        int y = height / 2;
        drawCentered(g, line1, width, y - (line2.isEmpty() ? 0 : 20), fm);
        if (!line2.isEmpty()) drawCentered(g, line2, width, y + 20, fm);

        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        return baos.toByteArray();
    }

    private void drawCentered(Graphics2D g, String text, int width, int y, FontMetrics fm) {
        int textWidth = fm.stringWidth(text);
        g.drawString(text, (width - textWidth) / 2, y);
    }
}