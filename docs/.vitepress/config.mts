import { defineConfig } from "vitepress";
import markdownIt from "markdown-it";
import markdownKatex from "markdown-it-katex";
import markdownPlantuml from "markdown-it-plantuml";
import markdownAdmonition from "markdown-it-admonition";
import markdownTaskLists from "markdown-it-task-lists";

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "Система аналізу медіа контенту",
  description: "Лабораторні роботи з дисципліни Організація баз даних",
  lang: "uk",
  base: "/edu_db_labs/",
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: "Головна", link: "/" },
      { text: "Зміст", link: "/intro/README" },
      { text: "Автори", link: "/README" },
    ],

    footer: {
      copyright: "MIT Licensed | Copyright © 2025 Shablo"
    },

    search: {
      provider: 'local'
    },

    sidebar: [
      {
        text: "Зміст",
        items: [
          { text: "Вступ", link: "/intro/README" },
          {
            text: "Система аналізу медіа контенту",
            items: [
              { text: "Аналіз предметної області", link: "/requirements/state-of-the-art" },
              {
                text: "Потреби зацікавлених сторін",
                link: "/requirements/stakeholders-needs",
              },
            ],
          },
          {
            text: "Розроблення вимог до функціональности системи",
            link: "/use cases/README",
          },
          {
            text: "Проектування інформаційного забезпечення",
            link: "/design/README",
          },
          {
            text: "Реалізація інформаційного та програмного забезпечення",
            link: "/software/README",
          },
          {
            text: "Реалізація об'єктно-реляційного відображення",
            link: "/src/role_db",
          },
          {
            text: "Тестування працездатності системи",
            link: "/test/README",
          },
          {
            text: "Висновки",
            link: "/conclusion/README",
          },
        ],
      },
    ],

    socialLinks: [
      { icon: "github", link: "https://github.com/SerdiukSacha/edu_db_lab6" },
    ],
  },

  markdown: {
    config: (md) => {
      md.set({ html: true })
      md.use(markdownKatex)
      md.use(markdownPlantuml)
      md.use(markdownAdmonition)
      md.use(markdownTaskLists)
    }
  }
});