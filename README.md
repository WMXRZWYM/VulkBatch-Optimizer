<div align="center">
  <img width="256" height="256" alt="V" src="https://github.com/user-attachments/assets/7a954d69-7d05-44be-9700-61b5bd474a7b" />

  <h1>VulkBatch</h1>
  <p>A lightweight client-side rendering optimization mod for Minecraft 26.3 (Vulkan/OpenGL).</p>

  <!-- Download Links -->
  <a href="https://www.curseforge.com/minecraft/mc-mods/vulkbatch">
    <img src="https://img.shields.io/badge/CurseForge-Download-orange?style=for-the-badge&logo=curseforge" alt="CurseForge">
  </a>
  <a href="https://modrinth.com/mod/vulkbatch">
    <img src="https://img.shields.io/badge/Modrinth-Download-green?style=for-the-badge&logo=modrinth" alt="Modrinth">
  </a>
  <a href="https://github.com/WMXRZWYM/VulkBatch-Optimizer/releases">
    <img src="https://img.shields.io/badge/GitHub-Releases-blue?style=for-the-badge&logo=github" alt="GitHub Releases">
  </a>
</div>

---

## ✨ Features

*   **BufferBuilder Batching:** Intercepts rendering builds to reuse memory allocations, significantly reducing memory and Garbage Collection (GC) pressure during heavy entity and particle rendering.
*   **Chunk Rebuild Optimization:** Introduces a 5-frame rolling window to skip redundant chunk rebuilds, saving CPU cycles during constant block updates (e.g., explosions, fast mining).
*   **Vulkan & OpenGL Support:** Fully compatible with both rendering backends introduced in Minecraft 26.x.

## ⚙️ Installation

1. Install **Fabric Loader** for Minecraft 26.3.
2. Download the `.jar` file and drop it into your `mods` folder.
3. Requires **Fabric API** as a dependency.

## 📥 Downloads

*   **CurseForge**: [Click here](https://www.curseforge.com/minecraft/mc-mods/vulkbatch)
*   **Modrinth**: [Click here](https://modrinth.com/mod/vulkbatch)
*   **GitHub Releases**: [Click here](https://github.com/WMXRZWYM/VulkBatch-Optimizer/releases)

## 📄 License

This project is licensed under the **MIT** License.
